plugins {
    kotlin("jvm")
    application
}

group = "org.progreso"

repositories {
    mavenCentral()
}

dependencies {
    api("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("com.google.code.gson:gson:2.10.1")
}

application {
    mainClass.set("org.progreso.irc.server.application.ApplicationKt")
}

tasks.jar {
    manifest.attributes(
        "Main-Class" to "org.progreso.irc.server.application.ApplicationKt"
    )
}