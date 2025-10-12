package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ElementListWidget
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
            button.onPress { Util.getOperatingSystem().open(Paths.get("mods").toFile()) }
        }

        button(i18n = "gui.plugins.button.done") { button ->
            button.dimensions(width / 2 + 4, height - 24, 150, 20)
            button.onPress { close() }
        }
    }

    private class PluginEntry(
        val parent: ElementListWidget<PluginEntry>,
        val plugin: AbstractPlugin
    ) : SimpleElementListEntry<PluginEntry>() {
        override fun render(context: DrawContext, x: Int, y: Int) = context {
            drawText(
                mc.textRenderer,
                plugin.name,
                x + 3,
                y + 3,
                Color.WHITE
            )
            drawText(
                mc.textRenderer,
                i18n("gui.plugins.label.plugin_version", plugin.version),
                x + 3,
                y + 4 + mc.textRenderer.fontHeight,
                Color.GRAY
            )
            drawText(
                mc.textRenderer,
                i18n("gui.plugins.label.plugin_author", plugin.author),
                x + 3,
                y + 5 + mc.textRenderer.fontHeight * 2,
                Color.GRAY
            )

            if (parent.selectedOrNull == this@PluginEntry) {
                drawBorder(x, y, width, height, Color.WHITE)
            }
        }

        override fun mouseClicked(click: Click?, doubled: Boolean): Boolean {
            parent.setSelected(this)

            return super.mouseClicked(click, doubled)
        }
    }
}