package com.jacobtread.kme.blaze.tdf

import io.netty.buffer.ByteBuf

class FloatTdf(label: String, override val value: Float) : Tdf<Float>(label, FLOAT) {
    companion object {
        fun read(label: String, input: ByteBuf): FloatTdf {
            val value = input.readFloat()
            return FloatTdf(label, value)
        }
    }

    override fun write(out: ByteBuf) {
        out.writeFloat(value)
    }

    override fun toString(): String = "Float($label: $value)"
}