package org.progreso.client.gui.minecraft.common

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ElementListWidget

open class SimpleElementListEntry<E : SimpleElementListEntry<E>> : ElementListWidget.Entry<E>() {
    open fun render(context: DrawContext, index: Int, x: Int, y: Int) {}

    override fun render(
        context: DrawContext,
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
        render(context, index, x, y)
    }

    override fun children(): MutableList<out Element> {
        return mutableListOf()
    }

    override fun selectableChildren(): MutableList<out Selectable> {
        return mutableListOf()
    }
}