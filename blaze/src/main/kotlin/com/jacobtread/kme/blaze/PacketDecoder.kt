package com.jacobtread.kme.blaze

import com.jacobtread.kme.utils.logging.Logger
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class PacketDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, input: ByteBuf, out: MutableList<Any>) {
        try {
            while (input.readableBytes() > 0) {
                val cursorStart = input.readerIndex() // The buffer position before reading
                val length = input.readUnsignedShort(); // Read the length of the packet
                // If we don't have enough buffer data loaded yet
                if (input.readableBytes() < (length + 10)) {
                    input.readerIndex(cursorStart) // Return the reading index and wait for more data
                    return
                }
                val component = input.readUnsignedShort() // Packet component id
                val command = input.readUnsignedShort() // Packet command id
                val error = input.readUnsignedShort() // Packet error
                val qtype = input.readUnsignedShort() // Packet qtype (should always be
                val id = input.readUnsignedShort()
                val extLength = if ((qtype and 0x10) != 0) input.readUnsignedShort() else 0
                val contentLength = length + (extLength shl 16)

                // If the current input has enough bytes to read the full content
                if (input.readableBytes() >= contentLength) { // If we have enough readable for the content
                    val content = Unpooled.buffer(contentLength, contentLength)
                    input.readBytes(content, contentLength)// Read the bytes into a new buffer and use that as content
                    val packet = Packet(component, command, error, qtype, id, content)
                    if (Logger.isLogPackets) {
                        try {
                            Logger.debug("RECEIVED PACKET =======\n" + packetToBuilder(packet) + "\n======================")
                        } catch (e: Throwable) {
                            Logger.warn("Failed to decode incoming packet contents for debugging: ")
                            Logger.warn("Packet Information ==================================")
                            Logger.warn("Component: ${packet.component.toString(16)} ${Components.getName(packet.component)}")
                            Logger.warn("Command: ${packet.command.toString(16)} ${Commands.getName(packet.component, packet.command)}")
                            Logger.warn("Error: ${packet.command.toString(16)}")
                            val typeName = when (packet.type) {
                                Packet.INCOMING_TYPE -> "INCOMING"
                                Packet.ERROR_TYPE -> "ERROR"
                                Packet.UNIQUE_TYPE -> "UNIQUE"
                                Packet.RESPONSE_TYPE -> "RESPONSE"
                                else -> "UNKNOWN"
                            }
                            Logger.warn("Type: $typeName (${packet.type.toString(16)})")
                            Logger.warn("ID: ${packet.id.toString(16)}")
                            Logger.warn("Cause: ${e.message}")
                            Logger.warn(e.stackTraceToString())
                            Logger.warn("=====================================================")
                        }
                    }
                    out.add(packet) // Add the packet to the output
                } else {
                    input.readerIndex(cursorStart)
                    return
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}