package com.jacobtread.kme.logging

enum class Level(val levelName: String, val index: Byte, val colorCode: String) {
    INFO("INFO", 4, "\u001B[34m"),
    WARN("WARN", 3, "\u001B[33m"),
    ERROR("ERROR", 2, "\u001B[31m"),
    FATAL("FATAL", 1, "\u001B[31m"),
    DEBUG("DEBUG", 5, "\u001B[30m");

    companion object {
        fun fromName(name: String): Level {
          return when(name.lowercase()) {
              "warn" -> WARN
              "error" -> ERROR
              "fatal" -> FATAL
              "debug" -> DEBUG
              else -> INFO
          }
        }
    }
}

