package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Util
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.Client.Companion.mc
import org.progreso.client.accessors.TextAccessor.i18n
import org.progreso.client.gui.builders.ButtonBuilder.Companion.button
import org.progreso.client.gui.builders.ElementListBuilder.Companion.elementList
import org.progreso.client.gui.drawText
import org.progreso.client.gui.invoke
import org.progreso.client.gui.minecraft.common.SimpleElementListEntry
import org.progreso.client.gui.minecraft.common.TitledScreen
import java.awt.Color
import java.nio.file.Paths

class ProgresoPluginsScreen(private val plugins: Set<AbstractPlugin>) : TitledScreen(i18n = "gui.plugins.title") {
    private var selectedPlugin: AbstractPlugin? = null

    override fun init() {
        elementList<PluginEntry> { list ->
            list.listDimension(
                x = width / 2 - 4 - 200,
                y = 24,
                width = 200,
                height = height - 28 - 24,
                itemHeight = 36
            )

            for (plugin in plugins) {
                list.addEntry(PluginEntry(plugin))
            }

            list.select {
                selectedPlugin = it?.plugin
            }
        }

        elementList<InfoEntry> { list ->
            list.listDimension(
                x = width / 2 + 4,
                y = 24,
                width = 200,
                height = height - 28 - 24,
                itemHeight = textRenderer.fontHeight + 1
            )

            var lastSelected: AbstractPlugin? = null
            list.render { _, _, _, _ ->
                if (selectedPlugin != lastSelected) {
                    lastSelected = selectedPlugin
                    children().clear()
                    scrollAmount = -Double.MAX_VALUE
                    if (lastSelected != null) {
                        children().add(
                            InfoEntry(
                                i18n("gui.plugins.label.plugin_name", lastSelected!!.name)
                            )
                        )
                        children().add(
                            InfoEntry(
                                i18n("gui.plugins.label.plugin_version", lastSelected!!.version)
                            )
                        )
                        children().add(
                            InfoEntry(
                                i18n("gui.plugins.label.plugin_author", lastSelected!!.author)
                            )
                        )
                    }
                }
            }
        }

        button(i18n = "gui.plugins.button.open_folder") { button ->
            button.dimensions(width / 2 - 154, height - 24, 150, 20)
            button.onPress { Util.getOperatingSystem().open(Paths.get("mods").toFile()) }
        }

        button(i18n = "gui.plugins.button.done") { button ->
            button.dimensions(width / 2 + 4, height - 24, 150, 20)
            button.onPress { close() }
        }
    }

    private class PluginEntry(val plugin: AbstractPlugin) : SimpleElementListEntry<PluginEntry>() {
        override fun render(context: DrawContext, index: Int, x: Int, y: Int) = context {
            drawText(
                mc.textRenderer,
                plugin.name,
                x + 3,
                y + 3,
                Color.WHITE
            )
            drawText(
                mc.textRenderer,
                plugin.author,
                x + 3,
                y + 3 + mc.textRenderer.fontHeight,
                Color.GRAY
            )
        }
    }

    private class InfoEntry(val text: String) : SimpleElementListEntry<InfoEntry>() {
        override fun render(context: DrawContext, index: Int, x: Int, y: Int) = context {
            drawText(mc.textRenderer, text, x + 3, y, Color.WHITE)
        }
    }
}