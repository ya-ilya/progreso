val minecraftVersion: String by project
val progresoVersion: String by project
val mixinVersion: String by project
val yarnMappings: String by project
val loaderVersion: String by project
val fabricVersion: String by project
val fabricKotlinVersion: String by project

plugins {
    kotlin("jvm")
    id("fabric-loom")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
}

group = "org.progreso"
version = progresoVersion

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

base {
    archivesName.set("progreso")
}

loom {
    accessWidenerPath.set(file("src/main/resources/progreso.accesswidener"))

    runConfigs.configureEach {
        ideConfigGenerated(true)
    }
}

val library: Configuration by configurations.creating

configurations {
    shadow.get().extendsFrom(library)
    implementation.get().extendsFrom(library)
}

dependencies {
    "minecraft"("com.mojang:minecraft:$minecraftVersion")
    "mappings"("net.fabricmc:yarn:$yarnMappings:v2")

    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    library("org.reflections:reflections:0.10.2")
    library(project(":progreso-api"))
    library(project(":progreso-irc"))
}

tasks {
    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "version" to version
                )
            )
        }
    }

    register<Jar>("buildApi") {
        group = "progreso"

        archiveClassifier.set("api")

        from(sourceSets["main"].output)
        from(project(":progreso-api").sourceSets["main"].output)
    }

    register<Jar>("buildApiSource") {
        group = "progreso"

        archiveClassifier.set("api-source")

        from(project.sourceSets["main"].allSource)
        from(project(":progreso-api").sourceSets["main"].allSource)
    }

    register("buildAll") {
        group = "progreso"

        dependsOn("buildApi", "buildApiSource", "build")
    }

    withType<JavaCompile>().configureEach {
        options.release.set(17)
    }

    shadowJar {
        configurations = listOf(project.configurations.shadow.get())

        dependencies {
            exclude {
                it.moduleGroup == "org.slf4j"
            }
        }

        relocate("kotlin", "org.progreso.shadow.kotlin")
        archiveClassifier.set("shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveClassifier.set("release")
    }
}

publishing {
    publications {
        create<MavenPublication>("api") {
            artifactId = "progreso"

            artifact(tasks["buildApi"]) { classifier = null }
            artifact(tasks["buildApiSource"]) { classifier = "sources" }
        }
    }
}