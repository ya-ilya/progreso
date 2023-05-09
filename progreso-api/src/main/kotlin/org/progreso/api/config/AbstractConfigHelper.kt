package org.progreso.api.config

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.progreso.api.Api
import org.progreso.api.config.container.AbstractConfigHelperContainer
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

/**
 * Config helper abstract class
 *
 * @param name Helper name
 * @param path Helper path
 * @param provider Config provider
 * @param container Helper container
 * @param defaultConfigName Default config name
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class AbstractConfigHelper<T : AbstractConfig>(
    name: String,
    path: String,
    private val provider: AbstractConfigProvider<T>,
    private val container: AbstractConfigHelperContainer,
    private val defaultConfigName: String? = null
) {
    val name = name.trim()
    val path: Path = Paths.get("progreso${File.separator}$path")
    val configs = mutableListOf<T>()

    init {
        refresh()
        checkDefault()
    }

    abstract fun read(name: String, reader: JsonReader): T
    abstract fun write(config: T, writer: JsonWriter)

    fun save() {
        for (config in configs.toList()) {
            save(config.name, false)
        }
    }

    fun save(name: String, setCurrent: Boolean = true) {
        checkCurrent()
        checkDirectory()

        configs.firstOrNull { it.name == name }.also { config ->
            var currentConfig = config ?: provider.create(name)

            if (currentConfig.name.equals(container.getHelperConfig(this), true)) {
                configs.remove(currentConfig)

                provider.create(name).also {
                    configs.add(it)
                    currentConfig = it
                }
            }

            writeConfig(name, currentConfig)

            if (setCurrent) {
                container.setHelperConfig(this, currentConfig.name)
            }
        }
    }

    fun load(name: String, createIfNotExists: Boolean = false) {
        checkCurrent()

        if (createIfNotExists && !configs.any { it.name == name }) {
            configs.add(provider.create(name))
        }

        configs.first { it.name == name }.also { config ->
            provider.apply(config)
            container.setHelperConfig(this, name)
        }
    }

    fun refresh() {
        checkDirectory()

        configs.clear()
        for (path in Files.walk(path)) {
            if (path.extension == "json") {
                configs.add(readConfig(path.nameWithoutExtension))
            }
        }
    }

    fun refresh(name: String) {
        configs.removeIf { it.name == name }
        configs.add(readConfig(name))
    }

    private fun checkDirectory() {
        if (!path.parent.exists()) {
            path.parent.createDirectories()
        }

        if (!path.exists()) {
            path.createDirectories()
        }
    }

    private fun checkCurrent() {
        val current = container.getHelperConfig(this)!!
        if (!configs.any { it.name == current }) {
            configs.add(provider.create(current))
        }
    }

    private fun checkDefault() {
        if (defaultConfigName != null) {
            if (!configs.any { it.name == defaultConfigName }) {
                provider.create(defaultConfigName).also {
                    configs.add(it)
                    writeConfig(defaultConfigName, it)
                }
            }
        }
    }

    private fun writeConfig(name: String, config: T) {
        val path = configPath(name)
        Api.GSON.newJsonWriter(path.writer()).use {
            write(config, it)
        }
    }

    private fun readConfig(name: String): T {
        val path = configPath(name)
        return Api.GSON.newJsonReader(path.reader()).use {
            read(path.nameWithoutExtension, it)
        }
    }

    private fun configPath(name: String) = Paths.get("$path${File.separator}$name.json")
}