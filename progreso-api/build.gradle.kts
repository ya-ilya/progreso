val progresoVersion: String by project
val gsonVersion: String by project
val brigadierVersion: String by project

plugins {
    kotlin("jvm")
}

group = "org.progreso"
version = progresoVersion

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

dependencies {
    api("com.mojang:brigadier:$brigadierVersion")
    api("com.google.code.gson:gson:$gsonVersion")
}