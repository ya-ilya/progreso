package org.progreso.api.managers

import org.progreso.api.Api
import org.progreso.api.config.categories.AltConfigCategory
import org.progreso.api.config.categories.FriendConfigCategory
import org.progreso.api.config.categories.ModuleConfigCategory
import org.progreso.api.config.container.ConfigCategoryContainer
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

object ConfigManager : ConfigCategoryContainer {
    const val DEFAULT_CONFIG_NAME = "default"

    override val categories = mutableSetOf(
        ModuleConfigCategory(container = ModuleManager),
        FriendConfigCategory(),
        AltConfigCategory()
    )

    private var globalConfigAccessor: GlobalConfigAccessor = GlobalConfigAccessor.Default

    fun load(globalConfigAccessor: GlobalConfigAccessor) {
        this.globalConfigAccessor = globalConfigAccessor
        this.load()
    }

    fun load() {
        globalConfigAccessor.fromJson(checkGlobalConfig().readText())

        for ((key, value) in globalConfigAccessor.categories) {
            getCategoryByName(key).load(value, true)
        }
    }

    fun save() {
        globalConfigAccessor.categories = categories.onEach { it.save() }.associate { it.name to it.config }

        checkGlobalConfig().writeText(globalConfigAccessor.toJson())
    }

    private fun checkGlobalConfig(): Path {
        val global = Paths.get("progreso${File.separator}global.json")

        global.createParentDirectories()

        if (global.notExists()) {
            global.createFile()
            save()
        }

        return global
    }

    interface GlobalConfigAccessor {
        object Default : GlobalConfigAccessor {
            override var categories: Map<String, String> = emptyMap()

            override fun fromJson(text: String) {
                categories = Api.GSON.fromJson<Map<String, String>>(text, Map::class.java)
            }

            override fun toJson(): String {
                return Api.GSON.toJson(categories)
            }
        }

        var categories: Map<String, String>

        fun fromJson(text: String)
        fun toJson(): String
    }
}