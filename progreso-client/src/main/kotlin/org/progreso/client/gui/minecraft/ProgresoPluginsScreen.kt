package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.util.Util
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.Client.Companion.mc
import org.progreso.client.accessors.TextAccessor.i18n
import org.progreso.client.gui.builders.ButtonBuilder.Companion.button
import org.progreso.client.gui.builders.ElementListBuilder.Companion.elementList
import org.progreso.client.gui.drawBorder
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
                x = width / 2 - 100,
                y = 24,
                width = 200,
                height = height - 28 - 24,
                itemHeight = 36
            )

            for (plugin in plugins) {
                list.addEntry {
                    PluginEntry(this, plugin)
                }
            }

            list.select {
                selectedPlugin = it?.plugin
            }
        }

        button(i18n = "gui.plugins.button.open_folder") { button ->
            button.dimensions(width / 2 - 154, height - 24, 150, 20)
            button.onPress { Util.getPlatform().openFile(Paths.get("mods").toFile()) }
        }

        button(i18n = "gui.plugins.button.done") { button ->
            button.dimensions(width / 2 + 4, height - 24, 150, 20)
            button.onPress { onClose() }
        }
    }

    private class PluginEntry(
        val parent: ContainerObjectSelectionList<PluginEntry>,
        val plugin: AbstractPlugin
    ) : SimpleElementListEntry<PluginEntry>() {
        override fun render(context: GuiGraphicsExtractor, x: Int, y: Int, width: Int, height: Int) = context {
            drawText(
                mc.font,
                plugin.name,
                x + 3,
                y + 3,
                Color.WHITE
            )
            drawText(
                mc.font,
                i18n("gui.plugins.label.plugin_version", plugin.version),
                x + 3,
                y + 4 + mc.font.lineHeight,
                Color.GRAY
            )
            drawText(
                mc.font,
                i18n("gui.plugins.label.plugin_author", plugin.author),
                x + 3,
                y + 5 + mc.font.lineHeight * 2,
                Color.GRAY
            )

            if (parent.selected == this@PluginEntry) {
                drawBorder(x, y, width, height, Color.WHITE)
            }
        }

        override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
            parent.selected = this

            return super.mouseClicked(event, doubleClick)
        }
    }
}