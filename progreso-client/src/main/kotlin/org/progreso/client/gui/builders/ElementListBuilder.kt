package org.progreso.client.gui.builders

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import org.progreso.api.gui.builders.AbstractElementListBuilder
import org.progreso.client.Client.Companion.mc

class ElementListBuilder<E : ContainerObjectSelectionList.Entry<E>>
    : AbstractElementListBuilder<GuiGraphicsExtractor, ContainerObjectSelectionList<E>, E>() {

    companion object {
        fun <E : ContainerObjectSelectionList.Entry<E>> Screen.elementList(block: (ElementListBuilder<E>) -> Unit): ContainerObjectSelectionList<E> {
            return this.addRenderableWidget(
                ElementListBuilder<E>().apply(block).build()
            )
        }
    }

    override fun build(): ContainerObjectSelectionList<E> {
        return object : ContainerObjectSelectionList<E>(mc.client, width, height, y, itemHeight) {
            init {
                this.x = this@ElementListBuilder.x

                for (entry in this@ElementListBuilder.children) {
                    addEntry(entry)
                }

                for (initializer in this@ElementListBuilder.childrenInitializers) {
                    addEntry(initializer(this))
                }

                listeners.init(this)
            }

            override fun getRowWidth(): Int {
                return this@ElementListBuilder.itemWidth
            }

            override fun scrollBarX(): Int {
                return this.x + this.width - 6
            }

            override fun extractWidgetRenderState(
                graphics: GuiGraphicsExtractor,
                mouseX: Int,
                mouseY: Int,
                delta: Float
            ) {
                listeners.render(this, graphics, mouseX, mouseY, delta)

                super.extractWidgetRenderState(graphics, mouseX, mouseY, delta)
            }

            override fun mouseClicked(event: MouseButtonEvent, doubled: Boolean): Boolean {
                listeners.mouseClicked(this, event.x.toInt(), event.y.toInt(), event.button())

                return super.mouseClicked(event, doubled)
            }

            override fun setSelected(entry: E?) {
                elementListListeners.select(this, entry)

                super.setSelected(entry)
            }

            override fun mouseReleased(event: MouseButtonEvent): Boolean {
                listeners.mouseReleased(this, event.x.toInt(), event.y.toInt(), event.button())

                return super.mouseReleased(event)
            }
        }
    }
}