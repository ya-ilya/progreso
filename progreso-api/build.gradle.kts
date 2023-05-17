plugins {
    kotlin("jvm")
}

group = "org.progreso"

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

dependencies {
    api("com.google.code.gson:gson:2.10.1")
    api("com.mojang:brigadier:1.0.18")
    api("org.java-websocket:Java-WebSocket:1.5.3")
}