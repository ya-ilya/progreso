package org.progreso.api.managers

import org.progreso.api.Api.GSON
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.config.container.AbstractConfigHelperContainer
import org.progreso.api.config.helpers.FriendConfigHelper
import org.progreso.api.config.helpers.ModuleConfigHelper
import org.progreso.api.config.providers.ModuleConfigProvider
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

object ConfigManager : AbstractConfigHelperContainer {
    const val DEFAULT_CONFIG_NAME = "default"

    override val helpers = mutableMapOf<AbstractConfigHelper<*>, String>(
        ModuleConfigHelper(provider = ModuleConfigProvider(ModuleManager)) to DEFAULT_CONFIG_NAME,
        FriendConfigHelper() to DEFAULT_CONFIG_NAME
    )

    init {
        load()
    }

    fun load() {
        for ((key, value) in GSON.fromJson<Map<String, String>>(checkConfigs().readText(), Map::class.java)) {
            this[key].load(value, true)
        }
    }

    fun save() {
        for (helper in helpers.keys) {
            helper.save()
        }

        checkConfigs().writeText(GSON.toJson(helpers.mapKeys { it.key.name }))
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