package org.progreso.client.gui.builders

import net.minecraft.client.gui.Click
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
        return object : ElementListWidget<E>(mc.client, width, height, y, itemHeight) {
            init {
                x = this@ElementListBuilder.x

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

            override fun getScrollbarX(): Int {
                return x + width - 6
            }

            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, context, mouseX, mouseY, delta)

                super.renderWidget(context, mouseX, mouseY, delta)
            }

            override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
                listeners.mouseClicked(this, click.x.toInt(), click.y.toInt(), click.button())

                return super.mouseClicked(click, doubled)
            }

            override fun setSelected(entry: E?) {
                elementListListeners.select(this, entry)

                super.setSelected(entry)
            }

            override fun mouseReleased(click: Click): Boolean {
                listeners.mouseReleased(this, click.x.toInt(), click.y.toInt(), click.button())

                return super.mouseReleased(click)
            }
        }
    }
}