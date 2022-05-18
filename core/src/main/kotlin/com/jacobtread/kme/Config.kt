package com.jacobtread.kme

import com.jacobtread.kme.database.DatabaseConfig
import com.jacobtread.kme.logging.Logger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.yamlkt.Comment
import net.mamoe.yamlkt.Yaml
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

/**
 * loadConfigFile Loads the configuration from the YAML config file
 * config.yml if the config file doesn't exist a new one is created
 *
 * @return The loaded config file or default config if none
 */
fun loadConfigFile(): Config {
    val config: Config
    val configFile = Path("config.yml")
    if (configFile.exists()) {
        val contents = configFile.readText()
        config = Yaml.decodeFromString(Config.serializer(), contents)
        Logger.info("Loaded Configuration from: $configFile")
    } else {
        Logger.info("No configuration found. Using default")
        config = Config()
        try {
            Logger.debug("Saving newly created config to: $configFile")
            configFile.writeText(Yaml.encodeToString(config))
        } catch (e: Exception) {
            Logger.error("Failed to write newly created config file", e)
        }
    }
    return config
}

@Serializable
data class Config(
    @Comment("The level of logging that should be used: INFO, WARN, ERROR, FATAL, DEBUG")
    @SerialName("log_level")
    val logLevel: String = "INFO",

    @Comment("Ports for the different child servers of this server")
    val ports: Ports = Ports(),


    @Comment(
        """
    The message displayed in the main menu format codes:
    {v} = version, {n} = player name, {ip} = player ip
    """
    )
    @SerialName("menu_message")
    val menuMessage: String = "<font color='#B2B2B2'>KME3</font> - <font color='#FFFF66'>Logged as: {n}</font>",

    @Comment("Database connection info")
    val database: DatabaseConfig = DatabaseConfig(),

    ) {


    /**
     * Ports Configuration section that stores the different ports
     * used by each service
     *
     * @property redirector The port for the redirector server
     * @property ticker The port for the ticker server
     * @property telemetry The port for the telemetry server
     * @property main The port for the main server
     * @constructor Create empty Ports
     */
    @Serializable
    data class Ports(
        @Comment(
            """
        The port the redirector server will listen on. NOTE: Clients will only
        connect to 42127 so changing this will make users unable to connect unless
        you are behind some sort of proxy that's mapping the port
        """
        )
        val redirector: Int = 42127,
        @Comment("Port for the ticker server")
        val ticker: Int = 8999,
        @Comment("Port for the telemetry server")
        val telemetry: Int = 9988,
        @Comment("Port for the main server")
        val main: Int = 14219,
    )
}