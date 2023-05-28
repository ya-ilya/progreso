package org.progreso.client.gui.minecraft.widgets

import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.util.math.MatrixStack
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen
import java.awt.Color

class ProgresoPluginInfoWidget(
    width: Int,
    height: Int,
    private val parent: ProgresoPluginsScreen
) : ElementListWidget<ProgresoPluginInfoWidget.InfoEntry>(
    mc,
    width,
    height,
    32,
    height - 55 + 4,
    mc.textRenderer.fontHeight + 1
) {
    private var lastSelected: AbstractPlugin? = null

    override fun getRowWidth(): Int {
        return width
    }

    override fun getScrollbarPositionX(): Int {
        return right - 6
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(matrices, mouseX, mouseY, delta)

        val selectedPlugin = parent.selectedPlugin
        if (selectedPlugin != lastSelected) {
            lastSelected = selectedPlugin
            clearEntries()
            scrollAmount = -Double.MAX_VALUE
            if (lastSelected != null) {
                children().add(InfoEntry("Name: ${lastSelected!!.name}"))
                children().add(InfoEntry("Version: ${lastSelected!!.version}"))
                children().add(InfoEntry("Author: ${lastSelected!!.author}"))
            }
        }
    }

    class InfoEntry(private val text: String) : Entry<InfoEntry>() {
        override fun render(
            matrices: MatrixStack?,
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
            mc.textRenderer.draw(matrices, text, x.toFloat() + 3f, y.toFloat(), Color.WHITE.rgb)
        }

        override fun children(): MutableList<out Element> {
            return mutableListOf()
        }

        override fun selectableChildren(): MutableList<out Selectable> {
            return mutableListOf()
        }
    }
}