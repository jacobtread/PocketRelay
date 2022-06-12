package com.jacobtread.kme.blaze.tdf

import com.jacobtread.kme.blaze.TdfContainer
import com.jacobtread.kme.utils.logging.Logger
import io.netty.buffer.ByteBuf

class GroupTdf(label: String, val start2: Boolean, override val value: List<Tdf<*>>) : Tdf<List<Tdf<*>>>(label, GROUP), TdfContainer {
    companion object {
        fun read(label: String, input: ByteBuf): GroupTdf {
            val out = ArrayList<Tdf<*>>()
            var start2 = false
            var byte: Int
            try {
                while (true) {
                    byte = input.readUnsignedByte().toInt()
                    if (byte == 0) break
                    if (byte == 2) {
                        start2 = true
                    } else {
                        input.readerIndex(input.readerIndex() - 1)
                    }
                    out.add(read(input))
                }
            } catch (e: Throwable) {
                Logger.error("Failed to read group tdf contents", e)
                if (out.isNotEmpty()) {
                    Logger.error("Last tdf in group was: " + out.last())
                }
                throw e
            }
            return GroupTdf(label, start2, out)
        }
    }

    override fun write(out: ByteBuf) {
        if (start2) out.writeByte(2)
        value.forEach {
            it.writeFully(out)
        }
        out.writeByte(0)
    }

    override fun getTdfByLabel(label: String): Tdf<*>? = value.find { it.label == label }
    override fun toString(): String = "Struct($label: $value)"
}