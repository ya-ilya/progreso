package org.progreso.api.plugin.loader

import java.net.URL
import java.net.URLClassLoader

class PluginClassLoader(parent: ClassLoader) : URLClassLoader(emptyArray(), parent) {
    public override fun addURL(url: URL) {
        super.addURL(url)
    }
}