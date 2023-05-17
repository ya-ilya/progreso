plugins {
    kotlin("jvm")
    application
}

group = "org.progreso"

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(project(":progreso-api"))
}

application {
    mainClass.set("org.progreso.irc.server.MainKt")
}

tasks.jar {
    manifest.attributes(
        "Main-Class" to "org.progreso.irc.server.MainKt"
    )
}