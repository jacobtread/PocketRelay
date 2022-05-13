package com.jacobtread.kme.database.repos

import com.jacobtread.kme.LOGGER
import com.jacobtread.kme.game.Player
import com.jacobtread.kme.utils.hashPassword
import java.sql.Connection
import java.sql.SQLException

class PlayerCreationException(reason: String, cause: Throwable? = null) : Exception(reason, cause)
class PlayerNotFoundException : Exception()
class ServerErrorException : Exception()

abstract class PlayersRepository : DatabaseRepository() {

    abstract fun isDisplayNameTaken(name: String): Boolean
    abstract fun isEmailTaken(email: String): Boolean
    abstract fun createPlayerInternal(email: String, displayName: String, hashedPassword: String)
    abstract fun getPlayerByName(displayName: String): Player

    fun createPlayer(email: String, displayName: String, password: String): Boolean {
        try {
            if (isEmailTaken(email)) throw PlayerCreationException("That email is already in use.")
            if (isDisplayNameTaken(displayName)) throw PlayerCreationException("That display name is already in use.")
            val hashed = hashPassword(password)
            createPlayerInternal(email, displayName, hashed)
            return true
        } catch (e: Exception) {
            LOGGER.error("Failed to create player", e)
            throw PlayerCreationException("Failed to create player", e)
        }
    }

    class MySQL(private val connection: Connection) : PlayersRepository() {
        override fun init() {
            try {
                val statement = connection.createStatement()
                statement.execute(
                    """
                    CREATE TABLE IF NOT EXISTS `players` (
                        `id` INT(255) AUTO_INCREMENT,
                        `email` VARCHAR(254),
                        `display_name` VARCHAR(99),
                        `credits` INT(10) unsigned,
                        `games_played` INT(10) unsigned DEFAULT 0,
                        `auth` VARCHAR(128) DEFAULT NULL,
                        `auth2` VARCHAR(128) DEFAULT NULL,
                        UNIQUE KEY `id_index` (`id`) USING BTREE,
                        UNIQUE KEY `name_index` (`display_name`) USING BTREE,
                        PRIMARY KEY (`id`)
                    );
                    """.trimIndent()
                )
                LOGGER.info("Created players database table")
                LOGGER.info("Creating example player")
            } catch (e: SQLException) {
                LOGGER.fatal("Failed to create players database table", e)
            }
        }

        override fun isDisplayNameTaken(name: String): Boolean {
            val statement = connection.prepareStatement("SELECT `id` FROM `players` WHERE display_name = ? LIMIT 1")
            statement.setString(1, name)
            val resultSet = statement.executeQuery()
            return resultSet.next()
        }

        override fun isEmailTaken(email: String): Boolean {
            val statement = connection.prepareStatement("SELECT `id` FROM `players` WHERE email = ? LIMIT 1")
            statement.setString(1, email)
            val resultSet = statement.executeQuery()
            return resultSet.next()
        }

        override fun createPlayerInternal(email: String, displayName: String, hashedPassword: String) {
            val statement = connection.prepareStatement("INSERT INTO `players` (`email`, `display_name`, `auth2`) VALUES (?, ?, ?)")
            statement.setString(1, email)
            statement.setString(2, displayName)
            statement.setString(3, hashedPassword)
            statement.executeUpdate()
        }

        override fun getPlayerByName(displayName: String): Player {
            try {
                if (isDisplayNameTaken(displayName)) throw PlayerNotFoundException()
                val statement = connection.prepareStatement("SELECT (`id`, `email`, `display_name`, `credits`, `games_played`, `auth`, `auth2`) FROM `players` WHERE `display_name` = ?")
                statement.setString(1, displayName)
                val result = statement.executeQuery()
                if (!result.next()) throw PlayerNotFoundException()
                return Player(
                    result.getInt("id"),
                    result.getString("email"),
                    result.getString("display_name"),
                    result.getInt("credits"),
                    result.getInt("games_played"),
                    result.getString("auth"),
                    result.getString("auth2"),
                )
            } catch (e: SQLException) {
                throw ServerErrorException()
            }
        }
    }

    class SQLite(private val connection: Connection): PlayersRepository() {
        override fun init() {
            TODO("Not yet implemented")
        }

        override fun isDisplayNameTaken(name: String): Boolean {
            TODO("Not yet implemented")
        }

        override fun isEmailTaken(email: String): Boolean {
            TODO("Not yet implemented")
        }

        override fun createPlayerInternal(email: String, displayName: String, hashedPassword: String) {
            TODO("Not yet implemented")
        }

        override fun getPlayerByName(displayName: String): Player {
            TODO("Not yet implemented")
        }
    }
}
