import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val mixinVersion: String by project
val forgeVersion: String by project
val mappingsChannel: String by project
val mappingsVersion: String by project

buildscript {
    repositories {
        mavenCentral()
        maven("https://files.minecraftforge.net/maven/")
        maven("https://repo.spongepowered.org/maven/")
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:4.+")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
        classpath("com.github.jengelman.gradle.plugins:shadow:6.1.0")
    }
}

plugins {
    kotlin("jvm")
}

apply(plugin = "net.minecraftforge.gradle")
apply(plugin = "org.spongepowered.mixin")
apply(plugin = "com.github.johnrengelman.shadow")

group = "org.progreso"

configure<net.minecraftforge.gradle.userdev.UserDevExtension> {
    mappings(mappingsChannel, mappingsVersion)

    runs {
        create("client") {
            workingDirectory(project.file("run"))

            property("fml.coreMods.load", "org.progreso.client.mixin.MixinLoader")
            property("mixin.env.disableRefMap", "true")

            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")
        }
    }
}

configure<org.spongepowered.asm.gradle.plugins.MixinExtension> {
    defaultObfuscationEnv = "searge"
    add(sourceSets["main"], "mixins.progreso.refmap.json")
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
}

configurations.create("library")

dependencies {
    "minecraft"("net.minecraftforge:forge:${forgeVersion}")

    "library"("org.spongepowered:mixin:${mixinVersion}") {
        exclude(module = "guava")
        exclude(module = "gson")
        exclude(module = "commons-io")
    }

    annotationProcessor("org.spongepowered:mixin:${mixinVersion}:processor") {
        exclude(module = "gson")
    }

    "library"("org.reflections:reflections:0.10.2")
    "library"(kotlin("stdlib-jdk8"))
    "library"(project(":progreso-api"))

    implementation(configurations["library"])
}

tasks.processResources {
    from(sourceSets["main"].resources.srcDirs) {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        include("mcmod.info")
        expand(
            mapOf(
                "version" to version,
                "mcversion" to "1.12.2"
            )
        )
    }
}

tasks {
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

    named<ShadowJar>("shadowJar") {
        manifest.attributes(
            mapOf(
                "Manifest-Version" to 1.0,
                "MixinConfigs" to "mixins.progreso.json",
                "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                "FMLCorePluginContainsFMLMod" to "true",
                "FMLCorePlugin" to "org.progreso.client.mixin.MixinLoader",
                "ForceLoadAsMod" to "true"
            )
        )

        exclude(
            "**/module-info.class",
            "DebugProbesKt.bin",
            "META-INF/versions/**",
            "META-INF/**.RSA",
            "META-INF/*.kotlin_module",
            "LICENSE.txt",
            "kotlin/**/*.kotlin_metadata",
            "kotlin/**/*.kotlin_builtins",
            "META-INF/*.version"
        )

        configurations = listOf(project.configurations["library"])
    }

    build {
        dependsOn("shadowJar")
    }
}