package org.progreso.api.plugin.loader

import org.progreso.api.plugin.AbstractPlugin
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

object PluginLoader {
    private val addURL = URLClassLoader::class.java
        .getDeclaredMethod("addURL", URL::class.java)
        .apply { isAccessible = true }

    fun loadPlugin(path: String): AbstractPlugin {
        val jarFile = JarFile(path)
        val pluginClassName = jarFile.manifest.mainAttributes.getValue("Plugin-Class")
            ?: throw RuntimeException("Plugin-Class missed in $path manifest")

        addURL(javaClass.classLoader, File(path).toURI().toURL())

        return Class.forName(pluginClassName).newInstance() as AbstractPlugin
    }
}