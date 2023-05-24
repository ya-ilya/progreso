package org.progreso.client.gui.mc

import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiUtilRenderComponents
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.client.GuiScrollingList
import org.lwjgl.input.Mouse
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.gui.mc.components.ProgresoGuiPluginList
import kotlin.math.max

class ProgresoGuiPlugins(private val plugins: List<AbstractPlugin>) : GuiScreen() {
    private companion object {
        const val SLOT_HEIGHT = 35
    }

    private var pluginList: ProgresoGuiPluginList? = null
    private var pluginInfo: GuiScrollingList? = null
    private var selected = -1
    private var selectedPlugin: AbstractPlugin? = null
    private var listWidth = 0

    override fun initGui() {
        for (plugin in plugins) {
            listWidth = max(listWidth, mc.fontRenderer.getStringWidth(plugin.name) + 10)
            listWidth = max(listWidth, mc.fontRenderer.getStringWidth(plugin.version) + 5 + SLOT_HEIGHT)
        }

        listWidth = listWidth.coerceAtLeast(110)
        pluginList = ProgresoGuiPluginList(this, plugins, listWidth, SLOT_HEIGHT)
        buttonList.add(GuiButton(0, (width - listWidth - 38) / 2 + listWidth - 70, height - 38, "Done"))
        updateSelectedPluginInfo()
    }

    override fun actionPerformed(button: GuiButton) {
        if (button.enabled) {
            when (button.id) {
                0 -> {
                    mc.displayGuiScreen(null)
                    return
                }
            }
        }

        super.actionPerformed(button)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        pluginList!!.drawScreen(mouseX, mouseY, partialTicks)
        if (pluginInfo != null) pluginInfo!!.drawScreen(mouseX, mouseY, partialTicks)
        val left = (width - listWidth - 38) / 2 + listWidth + 30
        drawCenteredString(fontRenderer, "Plugin List", left, 16, 0xFFFFFF)

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun handleMouseInput() {
        val mouseX = Mouse.getEventX() * width / this.mc.displayWidth
        val mouseY = height - Mouse.getEventY() * height / this.mc.displayHeight - 1
        if (pluginInfo != null) pluginInfo!!.handleMouseInput(mouseX, mouseY)
        pluginList!!.handleMouseInput(mouseX, mouseY)

        super.handleMouseInput()
    }

    fun selectIndex(index: Int) {
        if (index == selected) return
        selected = index
        selectedPlugin = if (index >= 0 && index <= plugins.size) plugins[selected] else null
        updateSelectedPluginInfo()
    }

    fun isIndexSelected(index: Int): Boolean {
        return index == selected
    }

    private fun updateSelectedPluginInfo() {
        pluginInfo = null
        if (selectedPlugin == null) return
        val lines = mutableListOf<String?>()
        lines.add(TextFormatting.WHITE.toString() + selectedPlugin!!.name)
        lines.add(TextFormatting.WHITE.toString() + "Version: " + selectedPlugin!!.version)
        pluginInfo = Info(this, width - listWidth - 30, lines)
    }

    private class Info(
        private val parent: ProgresoGuiPlugins,
        width: Int,
        lines: List<String?>
    ) : GuiScrollingList(
        parent.mc,
        width,
        parent.height,
        32,
        parent.height - 46,
        parent.listWidth + 20,
        60,
        parent.width,
        parent.height
    ) {
        private val lines = resizeContent(lines)

        init {
            setHeaderInfo(true, this.headerHeight)
        }

        override fun getSize(): Int {
            return 0
        }

        override fun isSelected(index: Int): Boolean {
            return false
        }

        override fun elementClicked(index: Int, doubleClick: Boolean) {}
        override fun drawBackground() {}
        override fun drawSlot(slotIdx: Int, entryRight: Int, slotTop: Int, slotBuffer: Int, tess: Tessellator?) {}

        private fun resizeContent(lines: List<String?>): List<ITextComponent?> {
            val result = mutableListOf<ITextComponent?>()
            for (line in lines) {
                if (line == null) {
                    result.add(null)
                    continue
                }
                val chat = ForgeHooks.newChatWithLinks(line, false)
                val maxTextLength = listWidth - 8
                if (maxTextLength >= 0) {
                    result.addAll(
                        GuiUtilRenderComponents.splitText(
                            chat,
                            maxTextLength,
                            parent.mc.fontRenderer,
                            false,
                            true
                        )
                    )
                }
            }
            return result
        }

        private val headerHeight: Int
            get() {
                var height = lines.size * 10
                if (height < bottom - top - 8) height = bottom - top - 8
                return height
            }

        override fun drawHeader(entryRight: Int, relativeY: Int, tess: Tessellator) {
            var top = relativeY
            for (line in lines) {
                if (line != null) {
                    GlStateManager.enableBlend()
                    parent.mc.fontRenderer.drawStringWithShadow(
                        line.formattedText,
                        (left + 4).toFloat(),
                        top.toFloat(),
                        0xFFFFFF
                    )
                    GlStateManager.disableAlpha()
                    GlStateManager.disableBlend()
                }
                top += 10
            }
        }

        override fun clickHeader(x: Int, y: Int) {
            if (y <= 0) return
            val lineIndex = y / 10
            if (lineIndex >= lines.size) return
            val line = lines[lineIndex]
            if (line != null) {
                for (part in line) {
                    if (part !is TextComponentString) continue
                    if (parent.mc.fontRenderer.getStringWidth(part.text) - 4 >= x) {
                        parent.handleComponentClick(part)
                        break
                    }
                }
            }
        }
    }
}