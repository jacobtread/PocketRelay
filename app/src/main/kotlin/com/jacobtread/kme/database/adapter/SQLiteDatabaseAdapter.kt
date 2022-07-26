package com.jacobtread.kme.database.adapter

import com.jacobtread.kme.database.RuntimeDriver
import com.jacobtread.kme.database.data.GalaxyAtWarData
import com.jacobtread.kme.database.data.Player
import com.jacobtread.kme.database.data.PlayerCharacter
import com.jacobtread.kme.database.data.PlayerClass
import com.jacobtread.kme.exceptions.DatabaseException
import com.jacobtread.kme.utils.logging.Logger
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.io.path.absolute
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists

class SQLiteDatabaseAdapter(file: String) : DatabaseAdapter {

    private val connection: Connection

    init {
        val version = "3.36.0.3"
        RuntimeDriver.createRuntimeDriver(
            "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/$version/sqlite-jdbc-$version.jar",
            "org.sqlite.JDBC",
            "sqlite.jar"
        )

        val path = Paths.get(file).absolute()
        val parentDir = path.parent
        if (parentDir.notExists()) parentDir.createDirectories()
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:$file")
        } catch (e: SQLException) {
            Logger.fatal("Unable to connect to SQLite database", e)
        }
    }

    private fun createDatabaseTables() {
        val tableSql = """
            | -- Players Table
            | CREATE TABLE IF NOT EXISTS `players`
            | (
            |     `id`              INTEGER
            |         CONSTRAINT players_pk
            |             PRIMARY KEY AUTOINCREMENT,
            |     `email`    TEXT NOT NULL,
            |     `display_name`    TEXT NOT NULL,
            |     `session_token`   TEXT    DEFAULT NULL,
            |     `password`        TEXT,
            |     `credits`         INTEGER DEFAULT 0,
            |     `credits_sepnt`   INTEGER DEFAULT 0,
            |     `games_played`    INTEGER DEFAULT 0,
            |     `seconds_played`  INTEGER DEFAULT 0,
            |     `inventory`       TEXT,
            |     `csreward`        INTEGER DEFAULT 0,
            |     `face_codes`      TEXT    DEFAULT '20;',
            |     `new_item`        TEXT    DEFAULT '20;4;',
            |     `completion`      TEXT    DEFAULT NULL,
            |     `progress`        TEXT    DEFAULT NULL,
            |     `cs_completion`   TEXT    DEFAULT NULL,
            |     `cs_timestamps_1` TEXT    DEFAULT NULL,
            |     `cs_timestamps_2` TEXT    DEFAULT NULL,
            |     `cs_timestamps_3` TEXT    DEFAULT NULL
            | );
            | 
            | -- Player Classes Table
            | CREATE TABLE IF NOT EXISTS `player_classes`
            | (
            |     `id`         INTEGER
            |         CONSTRAINT player_classes_pk
            |             PRIMARY KEY AUTOINCREMENT,
            |     `player_id`  INTEGER
            |         constraint player_classes_players_id_fk
            |             references players (`id`) NOT NULL,
            |     `index`      INTEGER              NOT NULL,
            |     `name`       TEXT                 NOT NULL,
            |     `level`      INTEGER              NOT NULL,
            |     `exp`        REAL                 NOT NULL,
            |     `promotions` INTEGER              NOT NULL
            | );
            | 
            | -- Player Characters Table
            | CREATE TABLE IF NOT EXISTS `player_characters`
            | (
            |     `id`                INTEGER
            |         CONSTRAINT player_characters_pk
            |             PRIMARY KEY AUTOINCREMENT,
            |     `player_id`         INTEGER
            |         CONSTRAINT player_characters_players_id_fk
            |             REFERENCES players (`id`) NOT NULL,
            |     `index`             INTEGER       NOT NULL,
            |     `kit_name`          TEXT          NOT NULL,
            |     `name`              TEXT          NOT NULL,
            |     `tint1`             INTEGER       NOT NULL,
            |     `tint2`             INTEGER       NOT NULL,
            |     `pattern`           INTEGER       NOT NULL,
            |     `pattern_color`      INTEGER       NOT NULL,
            |     `phong`             INTEGER       NOT NULL,
            |     `emissive`          INTEGER       NOT NULL,
            |     `skin_tone`         INTEGER       NOT NULL,
            |     `seconds_played`    INTEGER       NOT NULL,
            | 
            |     `timestamp_year`    INTEGER       NOT NULL,
            |     `timestamp_month`   INTEGER       NOT NULL,
            |     `timestamp_day`     INTEGER       NOT NULL,
            |     `timestamp_seconds` INTEGER       NOT NULL,
            | 
            |     `powers`            TEXT          NOT NULL,
            |     `hotkeys`           TEXT          NOT NULL,
            |     `weapons`           TEXT          NOT NULL,
            |     `weapon_mods`       TEXT          NOT NULL,
            | 
            |     `deployed`          INTEGER       NOT NULL,
            |     `leveled_up`        INTEGER       NOT NULL
            | );
            | 
            | -- Galaxy At War Table
            | CREATE TABLE IF NOT EXISTS `player_gaw`
            | (
            |     `id`            INTEGER
            |         CONSTRAINT player_classes_pk
            |             PRIMARY KEY AUTOINCREMENT,
            |     `player_id`     INTEGER
            |         constraint player_classes_players_id_fk
            |             references players (`id`) NOT NULL,
            |     `last_modified` INTEGER           NOT NULL,
            |     `group_a`       INTEGER           NOT NULL,
            |     `group_b`       INTEGER           NOT NULL,
            |     `group_c`       INTEGER           NOT NULL,
            |     `group_d`       INTEGER           NOT NULL,
            |     `group_e`       INTEGER           NOT NULL
            | );
        """.trimMargin()
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(tableSql)
        } catch (e: SQLException) {
            Logger.fatal("Failed to create database tables", e)
        }
    }

    override fun setup() {
        createDatabaseTables()
    }

    override fun isPlayerEmailTaken(email: String): Boolean {
        try {
            val statement = connection.prepareStatement("SELECT `id` FROM `players` WHERE email = ? LIMIT 1")
            statement.setString(1, email)
            val resultSet = statement.executeQuery()
            val result = resultSet.next()
            statement.close()
            return result
        } catch (e: SQLException) {
            throw DatabaseException("Exception in isPlayerEmailTaken", e)
        }
    }

    private fun getPlayerFromResultSet(resultSet: ResultSet): Player {
        return Player(
            playerId = resultSet.getInt("id"),
            email = resultSet.getString("email"),
            displayName = resultSet.getString("display_name"),
            password = resultSet.getString("password"),
            sessionToken = resultSet.getString("session_token"),
            credits = resultSet.getInt("credits"),
            creditsSpent = resultSet.getInt("credits_spent"),
            gamesPlayed = resultSet.getInt("games_played"),
            secondsPlayed = resultSet.getLong("seconds_played"),
            inventory = resultSet.getString("inventory"),
            csReward = resultSet.getInt("csreward"),
            faceCodes = resultSet.getString("face_codes"),
            newItem = resultSet.getString("new_item"),
            completion = resultSet.getString("completion"),
            progress = resultSet.getString("progress"),
            cscompletion = resultSet.getString("cs_completion"),
            cstimestamps1 = resultSet.getString("cs_timestamps_1"),
            cstimestamps2 = resultSet.getString("cs_timestamps_2"),
            cstimestamps3 = resultSet.getString("cs_timestamps_3"),
        )
    }

    override fun getPlayerById(id: Int): Player? {
        try {
            val statement = connection.prepareStatement("SELECT * FROM `players` WHERE `id` = ? LIMIT 1")
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()
            if (!resultSet.next()) return null
            val player = getPlayerFromResultSet(resultSet)
            statement.close()
            return player
        } catch (e: SQLException) {
            throw DatabaseException("Exception in getPlayerById", e)
        }
    }

    override fun getPlayerByEmail(email: String): Player? {
        try {
            val statement = connection.prepareStatement("SELECT * FROM `players` WHERE `email` = ? LIMIT 1")
            statement.setString(1, email)
            val resultSet = statement.executeQuery()
            if (!resultSet.next()) return null
            val player = getPlayerFromResultSet(resultSet)
            statement.close()
            return player
        } catch (e: SQLException) {
            throw DatabaseException("Exception in getPlayerById", e)
        }
    }

    override fun getPlayerBySessionToken(sessionToken: String): Player? {
        try {
            val statement = connection.prepareStatement("SELECT * FROM `players` WHERE `session_token` = ? LIMIT 1")
            statement.setString(1, sessionToken)
            val resultSet = statement.executeQuery()
            if (!resultSet.next()) return null
            val player = getPlayerFromResultSet(resultSet)
            statement.close()
            return player
        } catch (e: SQLException) {
            throw DatabaseException("Exception in getPlayerById", e)
        }
    }

    private fun getPlayerClassFromResultSet(resultSet: ResultSet): PlayerClass {
        return PlayerClass(
            index = resultSet.getInt("index"),
            name = resultSet.getString("name"),
            level = resultSet.getInt("level"),
            exp = resultSet.getFloat("exp"),
            promotions = resultSet.getInt("promotions"),
        )
    }

    override fun getPlayerClasses(player: Player): MutableList<PlayerClass> {
        try {
            val statement = connection.prepareStatement("SELECT * FROM `player_classes` WHERE `player_id` = ?")
            statement.setInt(1, player.playerId)
            val resultSet = statement.executeQuery()
            val results = ArrayList<PlayerClass>()
            while (resultSet.next()) {
                val value = getPlayerClassFromResultSet(resultSet)
                results.add(value)
            }
            statement.close()
            return results
        } catch (e: SQLException) {
            throw DatabaseException("Exception in getPlayerClasses", e)
        }
    }

    private fun getPlayerCharacterFromResultSet(resultSet: ResultSet): PlayerCharacter {
        return PlayerCharacter(
            index = resultSet.getInt("index"),
            kitName = resultSet.getString("kit_name"),
            name = resultSet.getString("name"),
            tint1 = resultSet.getInt("tint1"),
            tint2 = resultSet.getInt("tint2"),
            pattern = resultSet.getInt("pattern"),
            patternColor = resultSet.getInt("pattern_color"),
            phong = resultSet.getInt("phong"),
            emissive = resultSet.getInt("emissive"),
            skinTone = resultSet.getInt("skin_tone"),
            secondsPlayed = resultSet.getLong("seconds_played"),
            timestampYear = resultSet.getInt("timestamp_year"),
            timestampMonth = resultSet.getInt("timestamp_month"),
            timestampDay = resultSet.getInt("timestamp_day"),
            timestampSeconds = resultSet.getInt("timestamp_seconds"),
            powers = resultSet.getString("powers"),
            hotkeys = resultSet.getString("hotkeys"),
            weapons = resultSet.getString("weapons"),
            weaponMods = resultSet.getString("weapon_mods"),
            deployed = resultSet.getBoolean("deployed"),
            leveledUp = resultSet.getBoolean("leveled_up"),
        )
    }

    override fun getPlayerCharacters(player: Player): MutableList<PlayerCharacter> {
        try {
            val statement = connection.prepareStatement("SELECT * FROM `player_characters` WHERE `player_id` = ?")
            statement.setInt(1, player.playerId)
            val resultSet = statement.executeQuery()
            val results = ArrayList<PlayerCharacter>()
            while (resultSet.next()) {
                val value = getPlayerCharacterFromResultSet(resultSet)
                results.add(value)
            }
            statement.close()
            return results
        } catch (e: SQLException) {
            throw DatabaseException("Exception in getPlayerCharacters", e)
        }
    }

    override fun getGalaxyAtWarData(player: Player): GalaxyAtWarData {
        try {
            val statement = connection.prepareStatement("SELECT * FROM `player_gaw` WHERE `player_id` = ? LIMIT 1")
            statement.setInt(1, player.playerId)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val galaxyAtWarData = GalaxyAtWarData(
                    lastModified = resultSet.getLong("last_modified"),
                    groupA = resultSet.getInt("group_a"),
                    groupB = resultSet.getInt("group_b"),
                    groupC = resultSet.getInt("group_c"),
                    groupD = resultSet.getInt("group_d"),
                    groupE = resultSet.getInt("group_e"),
                )
                statement.close()
                return galaxyAtWarData
            } else {
                statement.close()
                val defaultData = GalaxyAtWarData.createDefault()
                setGalaxyAtWarData(player, defaultData)
                return defaultData
            }
        } catch (e: SQLException) {
            throw DatabaseException("Exception in getGalaxyAtWarData", e)
        }
    }

    override fun createPlayer(email: String, hashedPassword: String): Player {
        TODO("Not yet implemented")
    }

    override fun updatePlayerFully(player: Player) {
        TODO("Not yet implemented")
    }

    override fun setPlayerClass(player: Player, playerClass: PlayerClass) {
        TODO("Not yet implemented")
    }

    override fun setPlayerCharacter(player: Player, playerCharacter: PlayerCharacter) {
        TODO("Not yet implemented")
    }

    override fun setPlayerSessionToken(player: Player, sessionToken: String) {
        TODO("Not yet implemented")
    }

    override fun setUpdatedPlayerData(player: Player, key: String) {
        TODO("Not yet implemented")
    }

    override fun setGalaxyAtWarData(player: Player, galaxyAtWarData: GalaxyAtWarData) {
        TODO("Not yet implemented")
    }
}