@file:Suppress("UnstableApiUsage")

rootProject.name = "progreso"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

include(
    "progreso-api",
    "progreso-client",
    "progreso-irc-server"
)