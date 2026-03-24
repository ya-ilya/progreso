package org.progreso.client.gui.minecraft.common

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry

abstract class SimpleElementListEntry<E : SimpleElementListEntry<E>> : ContainerObjectSelectionList.Entry<E>() {
    open fun render(context: GuiGraphicsExtractor, x: Int, y: Int, width: Int, height: Int) {}

    override fun extractContent(
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        hovered: Boolean,
        delta: Float
    ) {
        render(graphics, x, y, contentWidth, contentHeight)
    }

    override fun narratables(): List<NarratableEntry> = emptyList()

    override fun children(): List<GuiEventListener> = emptyList()
}