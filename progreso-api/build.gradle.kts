val gsonVersion: String by project
val brigadierVersion: String by project

plugins {
    kotlin("jvm")
}

group = "org.progreso"

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation("com.mojang:brigadier:$brigadierVersion")
    api("com.google.code.gson:gson:$gsonVersion")
}