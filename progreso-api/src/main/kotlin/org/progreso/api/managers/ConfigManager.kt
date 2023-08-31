package org.progreso.api.managers

import org.progreso.api.Api.GSON
import org.progreso.api.config.AbstractConfigCategory
import org.progreso.api.config.categories.AltConfigCategory
import org.progreso.api.config.categories.FriendConfigCategory
import org.progreso.api.config.categories.ModuleConfigCategory
import org.progreso.api.config.container.ConfigCategoryContainer
import org.progreso.api.config.providers.ModuleConfigProvider
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

object ConfigManager : ConfigCategoryContainer {
    const val DEFAULT_CONFIG_NAME = "default"

    override val categories = mutableMapOf<AbstractConfigCategory<*>, String>(
        ModuleConfigCategory(provider = ModuleConfigProvider(ModuleManager)) to DEFAULT_CONFIG_NAME,
        FriendConfigCategory() to DEFAULT_CONFIG_NAME,
        AltConfigCategory() to DEFAULT_CONFIG_NAME
    )

    init {
        load()
    }

    fun load() {
        for ((key, value) in GSON.fromJson<Map<String, String>>(checkConfigs().readText(), Map::class.java)) {
            getCategoryByName(key).load(value, true)
        }
    }

    fun save() {
        for (category in categories.keys) {
            category.save()
        }

        checkConfigs().writeText(GSON.toJson(categories.mapKeys { it.key.name }))
    }

    private fun checkConfigs(): Path {
        val configs = Paths.get("progreso${File.separator}configs.json")

        configs.parent.createDirectories()

        if (configs.notExists()) {
            configs.createFile()
            save()
        }

        return configs
    }
}