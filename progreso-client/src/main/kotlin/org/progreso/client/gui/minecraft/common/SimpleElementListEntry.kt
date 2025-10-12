package org.progreso.client.gui.minecraft.common

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ElementListWidget

open class SimpleElementListEntry<E : SimpleElementListEntry<E>> : ElementListWidget.Entry<E>() {
    open fun render(context: DrawContext, x: Int, y: Int) {}

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        hovered: Boolean,
        deltaTicks: Float
    ) {
        render(context, x, y)
    }

    override fun children(): MutableList<out Element> {
        return mutableListOf()
    }

    override fun selectableChildren(): MutableList<out Selectable> {
        return mutableListOf()
    }
}