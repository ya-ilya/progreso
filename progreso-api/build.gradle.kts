val gsonVersion: String by project

plugins {
    kotlin("jvm")
}

group = "org.progreso"

repositories {
    mavenCentral()
}

dependencies {
    api("com.google.code.gson:gson:$gsonVersion")
}