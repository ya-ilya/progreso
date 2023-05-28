package org.progreso.client.mixin

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.api.plugin.loader.PluginLoader
import org.spongepowered.asm.mixin.Mixins
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.*

@Suppress("unused")
class MixinLoader : PreLaunchEntrypoint {
    override fun onPreLaunch() {
        val pluginsPath = Paths.get("progreso" + File.separator + "plugins")

        if (!pluginsPath.exists()) {
            pluginsPath.createDirectories()
        }

        FabricLoader.getInstance().getEntrypointContainers("progreso", AbstractPlugin::class.java)

        pluginsPath
            .listDirectoryEntries()
            .filter { !it.isDirectory() }
            .filter { it.extension == "jar" }
            .forEach {
                val plugin = PluginLoader.loadPlugin(it.absolutePathString())

                for (mixinConfig in plugin.mixinConfigs) {
                    Mixins.addConfiguration(mixinConfig)
                }

                PluginManager.addPlugin(plugin)
            }
    }
}