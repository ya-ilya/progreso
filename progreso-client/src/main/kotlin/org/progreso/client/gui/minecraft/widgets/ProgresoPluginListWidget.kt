package org.progreso.client.gui.minecraft.widgets

import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.util.math.MatrixStack
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen
import java.awt.Color

class ProgresoPluginListWidget(
    width: Int,
    height: Int,
    plugins: List<AbstractPlugin>,
    private val screen: ProgresoPluginsScreen
) : ElementListWidget<ProgresoPluginListWidget.PluginListEntry>(mc, width, height, 32, height - 55 + 4, 36) {
    init {
        for (plugin in plugins) {
            addEntry(PluginListEntry(plugin))
        }

        setRenderSelection(true)
    }

    override fun getRowWidth(): Int {
        return width
    }

    override fun getScrollbarPositionX(): Int {
        return right - 6
    }

    override fun isSelectedEntry(index: Int): Boolean {
        return index == children().indexOf(selectedOrNull ?: return false)
    }

    override fun setSelected(entry: PluginListEntry?) {
        super.setSelected(entry)

        screen.selectPlugin(entry?.plugin)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        setSelected(getEntryAtPosition(mouseX, mouseY))

        return super.mouseClicked(mouseX, mouseY, button)
    }

    class PluginListEntry(val plugin: AbstractPlugin) : Entry<PluginListEntry>() {
        override fun render(
            matrices: MatrixStack,
            index: Int,
            y: Int,
            x: Int,
            entryWidth: Int,
            entryHeight: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            tickDelta: Float
        ) {
            mc.textRenderer.draw(matrices, plugin.name, x + 3f, y + 3f, Color.WHITE.rgb)
            mc.textRenderer.draw(matrices, plugin.author, x + 3f, y + 3f + mc.textRenderer.fontHeight, Color.GRAY.rgb)
        }

        override fun children(): MutableList<out Element> {
            return mutableListOf()
        }

        override fun selectableChildren(): MutableList<out Selectable> {
            return mutableListOf()
        }
    }
}