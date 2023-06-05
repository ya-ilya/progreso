package org.progreso.client.gui.builders

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.util.math.MatrixStack
import org.progreso.api.gui.builders.AbstractElementListBuilder
import org.progreso.client.Client.Companion.mc
import org.progreso.client.mixins.accessors.AccessorScreen
import org.progreso.client.util.render.RenderContext

class ElementListBuilder<E : ElementListWidget.Entry<E>> : AbstractElementListBuilder<ElementListWidget<E>, E>() {
    companion object {
        fun <E : ElementListWidget.Entry<E>> Screen.elementList(block: (ElementListBuilder<E>) -> Unit): ElementListWidget<E> {
            return (this as AccessorScreen).addDrawableChildInvoker(
                ElementListBuilder<E>().apply(block).build()
            )
        }
    }

    override fun build(): ElementListWidget<E> {
        return object : ElementListWidget<E>(mc, width, height, top, bottom, itemHeight) {
            init {
                setLeftPos(this@ElementListBuilder.left)

                for (entry in this@ElementListBuilder.children) {
                    addEntry(entry)
                }

                if (this@ElementListBuilder.renderHeader) {
                    setRenderHeader(true, this@ElementListBuilder.headerHeight)
                }

                setRenderSelection(this@ElementListBuilder.renderSelection)
                listeners.init(this)
            }

            override fun getRowWidth(): Int {
                return this@ElementListBuilder.itemWidth
            }

            override fun isSelectedEntry(index: Int): Boolean {
                return index == children().indexOf(selectedOrNull ?: return false)
            }

            override fun setSelected(entry: E?) {
                if (entry == null) return
                super.setSelected(entry)

                elementListListeners.select(this, entry)
            }

            override fun renderHeader(matrices: MatrixStack, x: Int, y: Int) {
                elementListListeners.renderHeader(this, RenderContext(matrices), x, y)

                super.renderHeader(matrices, x, y)
            }

            override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, RenderContext(matrices), mouseX, mouseY)

                super.render(matrices, mouseX, mouseY, delta)
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