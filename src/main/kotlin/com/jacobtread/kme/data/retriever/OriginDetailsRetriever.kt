package com.jacobtread.kme.data.retriever

import com.jacobtread.blaze.*
import com.jacobtread.blaze.packet.Packet
import com.jacobtread.kme.data.blaze.Commands
import com.jacobtread.kme.data.blaze.Components
import com.jacobtread.kme.utils.logging.Logger
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * System for connecting to the official servers to attempt to
 * retrieve the details of an origin account.
 *
 * @constructor Create empty Origin details retriever
 */
object OriginDetailsRetriever {

    var isDataFetchingEnabled: Boolean = false
        internal set

    /**
     * Origin details object structure.
     *
     * @property email The email for the origin account
     * @property displayName The display name for the origin account
     * @property token The origin session token
     */
    data class OriginDetails(
        val email: String,
        val displayName: String,
        val token: String,
        val dataMap: HashMap<String, String>,
    )

    /**
     * Connects to the official origin servers and logs in
     * using the provided origin token. Returns a [OriginDetails]
     * object with the details or a default one in case of failure.
     *
     * @param token The origin token
     * @return The origin details
     */
    fun retrieve(token: String): OriginDetails {
        var serverChannel: Channel? = null
        var originDetails: OriginDetails? = null
        try {
            serverChannel = Retriever.createOfficialChannel(object : ChannelInboundHandlerAdapter() {
                override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                    try {
                        if (msg !is Packet) {
                            super.channelRead(ctx, msg)
                            return
                        }

                        // Authentication response
                        if (msg.component == Components.AUTHENTICATION
                            && msg.command == Commands.ORIGIN_LOGIN
                        ) {
                            // Parse the authentication details
                            val sessionGroup = msg.group("SESS")
                            val email = sessionGroup.text("MAIL")

                            val personaData = sessionGroup.group("PDTL")
                            val displayName = personaData.text("DSNM")

                            // Set the origin details
                            originDetails = OriginDetails(
                                email,
                                displayName,
                                token,
                                HashMap()
                            )

                            if (isDataFetchingEnabled) {
                                // Send a request for the user settings
                                val packet = clientPacket(Components.UTIL, Commands.USER_SETTINGS_LOAD_ALL, 0x2) {}
                                ctx.writeAndFlush(packet)
                            } else {
                                // Close to mark as finished
                                ctx.close()
                            }
                        }

                        if (isDataFetchingEnabled) {
                            if (msg.component == Components.UTIL && msg.command == Commands.USER_SETTINGS_LOAD_ALL) {
                                val settings = msg.map<String, String>("SMAP")
                                originDetails?.dataMap?.putAll(settings)

                                // Close to mark as finished
                                ctx.close()
                            }
                        }
                    } catch (e: MissingTdfException) {
                        ctx.close()
                    } catch (e: InvalidTdfException) {
                        ctx.close()
                    }
                }
            })
            if (serverChannel != null) {
                // Send the origin authentication packet
                val packet = clientPacket(
                    Components.AUTHENTICATION,
                    Commands.ORIGIN_LOGIN,
                    0x1
                ) {
                    text("AUTH", token)
                    number("TYPE", 0x1)
                }

                serverChannel.writeAndFlush(packet)
                serverChannel.closeFuture()
                    .await(8, TimeUnit.SECONDS)
            }
            if (originDetails != null) return originDetails!!
        } catch (_: InterruptedException) {
        }
        val uuid = UUID.randomUUID()
        val displayName = "Origin User ($uuid)"
        if (serverChannel != null) {
            Logger.warn("Failed to retrieve origin information for account. Defaulting to: $displayName")
        }
        return OriginDetails(
            displayName,
            displayName.take(99),
            token,
            HashMap()
        )
    }
}
