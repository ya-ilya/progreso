val progresoVersion: String by project
val websocketVersion: String by project
val gsonVersion: String by project

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
}

group = "org.progreso"
version = progresoVersion

repositories {
    mavenCentral()
}

dependencies {
    api("org.java-websocket:Java-WebSocket:$websocketVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
}

tasks {
    shadowJar {
        archiveBaseName.set("shadow")
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    jar {
        archiveFileName.set("${project.name}-release.jar")

        manifest.attributes(
            "Main-Class" to "org.progreso.irc.application.ApplicationKt"
        )
    }
}