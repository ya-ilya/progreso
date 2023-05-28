package org.progreso.api.plugin.loader

import org.progreso.api.plugin.AbstractPlugin
import java.io.File
import java.util.jar.JarFile

object PluginLoader {
    private val loader = PluginClassLoader(javaClass.classLoader)

    fun loadPlugin(path: String): AbstractPlugin {
        val jarFile = JarFile(path)
        val pluginClassName = jarFile.manifest.mainAttributes.getValue("Plugin-Class")
            ?: throw RuntimeException("Plugin-Class missed in $path manifest")

        loader.addURL(File(path).toURI().toURL())

        return loader.loadClass(pluginClassName).getDeclaredConstructor().newInstance() as AbstractPlugin
    }
}