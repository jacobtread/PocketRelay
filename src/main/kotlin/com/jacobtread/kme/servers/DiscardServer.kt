package com.jacobtread.kme.servers

import com.jacobtread.kme.utils.logging.Logger
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.io.IOException

/**
 * startDiscardServer Simple discard server. Reads all the input bytes
 * and throws away the contents
 */
fun startDiscardServer(bossGroup: NioEventLoopGroup, workerGroup: NioEventLoopGroup, ports: IntArray) {
    try {
        val bootstrap = ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {
                    ch.pipeline()
                        .addLast(object : ChannelInboundHandlerAdapter() {
                            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                                if (msg is ByteBuf) {
                                    val readable = msg.readableBytes()
                                    if (readable > 0) {
                                        val out = ByteArray(readable)
                                        msg.readBytes(out)
                                        ctx.flush()
                                        if (Logger.isDebugEnabled) {
                                            Logger.debug(out.contentToString())
                                        }
                                    }
                                }
                            }
                        })
                }
            })

        ports.forEach { port -> bootstrap.bind(port).sync() }
        Logger.info("Bound discard server to ports ${ports.contentToString()}")
    } catch (e: IOException) {
        Logger.error("Exception in discard server", e)
    }
}