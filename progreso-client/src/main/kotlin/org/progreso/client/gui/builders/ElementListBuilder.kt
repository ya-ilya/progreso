package org.progreso.client.gui.builders

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ElementListWidget
import org.progreso.api.gui.builders.AbstractElementListBuilder
import org.progreso.client.Client.Companion.mc

class ElementListBuilder<E : ElementListWidget.Entry<E>>
    : AbstractElementListBuilder<DrawContext, ElementListWidget<E>, E>() {
    companion object {
        fun <E : ElementListWidget.Entry<E>> Screen.elementList(block: (ElementListBuilder<E>) -> Unit): ElementListWidget<E> {
            return addDrawableChild(
                ElementListBuilder<E>().apply(block).build()
            )
        }
    }

    override fun build(): ElementListWidget<E> {
        return if (renderHeader) {
            object :
                ElementListWidget<E>(mc.client, width, height, y, itemHeight, this@ElementListBuilder.headerHeight) {
                init {
                    x = this@ElementListBuilder.x
                    children().addAll(this@ElementListBuilder.children)
                    listeners.init(this)
                }

                override fun getRowWidth(): Int {
                    return this@ElementListBuilder.itemWidth
                }

                override fun getScrollbarX(): Int {
                    return x + width - 6
                }

                override fun isSelectedEntry(index: Int): Boolean {
                    return index == children().indexOf(selectedOrNull ?: return false)
                }

                override fun setSelected(entry: E?) {
                    if (entry == null) return
                    super.setSelected(entry)

                    elementListListeners.select(this, entry)
                }

                override fun renderHeader(context: DrawContext, x: Int, y: Int) {
                    elementListListeners.renderHeader(this, context, x, y)

                    super.renderHeader(context, x, y)
                }

                override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                    listeners.render(this, context, mouseX, mouseY, delta)

                    super.renderWidget(context, mouseX, mouseY, delta)
                }

                override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
                    setSelected(getEntryAtPosition(mouseX, mouseY))
                    listeners.mouseClicked(this, mouseX.toInt(), mouseY.toInt(), button)

                    return super.mouseClicked(mouseX, mouseY, button)
                }

                override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
                    listeners.mouseReleased(this, mouseX.toInt(), mouseY.toInt(), button)

                    return super.mouseReleased(mouseX, mouseY, button)
                }
            }
        } else {
            object : ElementListWidget<E>(mc.client, width, height, y, itemHeight) {
                init {
                    x = this@ElementListBuilder.x
                    children().addAll(this@ElementListBuilder.children)
                    listeners.init(this)
                }

                override fun getRowWidth(): Int {
                    return this@ElementListBuilder.itemWidth
                }

                override fun getScrollbarX(): Int {
                    return x + width - 6
                }

                override fun isSelectedEntry(index: Int): Boolean {
                    return index == children().indexOf(selectedOrNull ?: return false)
                }

                override fun setSelected(entry: E?) {
                    if (entry == null) return
                    super.setSelected(entry)

                    elementListListeners.select(this, entry)
                }

                override fun renderHeader(context: DrawContext, x: Int, y: Int) {
                    elementListListeners.renderHeader(this, context, x, y)

                    super.renderHeader(context, x, y)
                }

                override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                    listeners.render(this, context, mouseX, mouseY, delta)

                    super.renderWidget(context, mouseX, mouseY, delta)
                }

                override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
                    setSelected(getEntryAtPosition(mouseX, mouseY))
                    listeners.mouseClicked(this, mouseX.toInt(), mouseY.toInt(), button)

                    return super.mouseClicked(mouseX, mouseY, button)
                }

                override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
                    listeners.mouseReleased(this, mouseX.toInt(), mouseY.toInt(), button)

                    return super.mouseReleased(mouseX, mouseY, button)
                }
            }
        }
    }
}