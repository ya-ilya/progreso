rootProject.name = "progreso"

pluginManagement {
    val kotlinVersion: String by settings
    val fabricLoomVersion: String by settings
    val shadowVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        id("fabric-loom") version fabricLoomVersion
        id("com.gradleup.shadow") version shadowVersion
    }
}

if (gradle.startParameter.taskNames.any { it.contains("progreso-irc:shadowJar") }) {
    include("progreso-irc")
    project(":progreso-irc").projectDir = file("progreso-irc")
} else {
    include(
        "progreso-api",
        "progreso-client",
        "progreso-irc"
    )
}