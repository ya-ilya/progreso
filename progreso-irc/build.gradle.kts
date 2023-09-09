val websocketVersion: String by project
val gsonVersion: String by project

plugins {
    kotlin("jvm")
    application
}

group = "org.progreso"

repositories {
    mavenCentral()
}

dependencies {
    api("org.java-websocket:Java-WebSocket:$websocketVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
}

application {
    mainClass.set("org.progreso.irc.application.ApplicationKt")
}

tasks.jar {
    manifest.attributes(
        "Main-Class" to "org.progreso.irc.application.ApplicationKt"
    )
}