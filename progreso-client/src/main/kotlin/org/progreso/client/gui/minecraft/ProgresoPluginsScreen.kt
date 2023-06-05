package org.progreso.client.gui.minecraft

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Util
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.builders.ButtonBuilder.Companion.button
import org.progreso.client.gui.builders.ElementListBuilder.Companion.elementList
import org.progreso.client.gui.minecraft.common.SimpleElementListEntry
import org.progreso.client.gui.minecraft.common.TitledScreen
import java.awt.Color
import java.io.File
import java.nio.file.Paths

class ProgresoPluginsScreen(private val plugins: List<AbstractPlugin>) : TitledScreen("Plugins") {
    private var selectedPlugin: AbstractPlugin? = null

    override fun init() {
        elementList<PluginEntry> { list ->
            list.left = width / 2 - 4 - 200
            list.listDimension(200, height, 24, height - 28, 36)

            for (plugin in plugins) {
                list.addEntry(PluginEntry(plugin))
            }
        }

        elementList<InfoEntry> { list ->
            list.left = width / 2 + 4
            list.listDimension(200, height, 24, height - 28, textRenderer.fontHeight + 1)

            var lastSelected: AbstractPlugin? = null
            list.render { _, _, _ ->
                if (selectedPlugin != lastSelected) {
                    lastSelected = selectedPlugin
                    children().clear()
                    scrollAmount = -Double.MAX_VALUE
                    if (lastSelected != null) {
                        children().add(InfoEntry("Name: ${lastSelected!!.name}"))
                        children().add(InfoEntry("Version: ${lastSelected!!.version}"))
                        children().add(InfoEntry("Author: ${lastSelected!!.author}"))
                    }
                }
            }
        }

        button("Open Folder") { button ->
            button.dimensions(width / 2 - 154, height - 24, 150, 20)
            button.onPress { Util.getOperatingSystem().open(Paths.get("progreso${File.separator}plugins").toFile()) }
        }

        button("Done") { button ->
            button.dimensions(width / 2 + 4, height - 24, 150, 20)
            button.onPress { close() }
        }
    }

    private class PluginEntry(val plugin: AbstractPlugin) : SimpleElementListEntry<PluginEntry>() {
        override fun render(matrices: MatrixStack, index: Int, x: Int, y: Int) {
            mc.textRenderer.draw(matrices, plugin.name, x + 3f, y + 3f, Color.WHITE.rgb)
            mc.textRenderer.draw(
                matrices,
                plugin.author,
                x + 3f,
                y + 3f + mc.textRenderer.fontHeight,
                Color.GRAY.rgb
            )
        }
    }

    private class InfoEntry(val text: String) : SimpleElementListEntry<InfoEntry>() {
        override fun render(matrices: MatrixStack, index: Int, x: Int, y: Int) {
            mc.textRenderer.draw(matrices, text, x.toFloat() + 3f, y.toFloat(), Color.WHITE.rgb)
        }
    }
}