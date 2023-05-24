package org.progreso.client.gui.mc.components

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.StringUtils
import net.minecraftforge.fml.client.GuiScrollingList
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.gui.mc.ProgresoGuiPlugins

class ProgresoGuiPluginList(
    private val parent: ProgresoGuiPlugins,
    private val plugins: List<AbstractPlugin>,
    listWidth: Int,
    slotHeight: Int
) : GuiScrollingList(
    parent.mc,
    listWidth,
    parent.height,
    32,
    parent.height - 46,
    10,
    slotHeight,
    parent.width,
    parent.height
) {
    override fun getSize(): Int {
        return plugins.size
    }

    override fun elementClicked(index: Int, doubleClick: Boolean) {
        parent.selectIndex(index)
    }

    override fun isSelected(index: Int): Boolean {
        return parent.isIndexSelected(index)
    }

    override fun drawBackground() {
        parent.drawDefaultBackground()
    }

    override fun getContentHeight(): Int {
        return size * 35 + 1
    }

    override fun drawSlot(index: Int, right: Int, top: Int, height: Int, tessellator: Tessellator?) {
        val plugin = plugins[index]
        val name = StringUtils.stripControlCodes(plugin.name)
        val version = StringUtils.stripControlCodes(plugin.version)
        val fontRenderer: FontRenderer = parent.mc.fontRenderer

        fontRenderer.drawString(fontRenderer.trimStringToWidth(name, listWidth - 10), left + 3, top + 2, 0xFFFFFF)
        fontRenderer.drawString(
            fontRenderer.trimStringToWidth(version, listWidth - (5 + height)),
            left + 3,
            top + 12,
            0xCCCCCC
        )
    }
}