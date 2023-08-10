rootProject.name = "progreso"

pluginManagement {
    val kotlinVersion: String by settings
    val fabricLoomVersion: String by settings

    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        id("fabric-loom") version fabricLoomVersion
    }
}

include(
    "progreso-api",
    "progreso-client",
    "progreso-irc"
)