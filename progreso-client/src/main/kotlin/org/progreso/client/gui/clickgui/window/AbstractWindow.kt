package org.progreso.client.gui.clickgui.window

import net.minecraft.client.gui.DrawContext
import org.progreso.api.common.ObservableCollection
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.clickgui.element.data.ElementOffsets
import org.progreso.client.gui.drawCenteredString
import org.progreso.client.gui.drawHorizontalLine
import org.progreso.client.gui.drawRect
import org.progreso.client.gui.invoke
import java.awt.Color

@Suppress("SuspiciousVarProperty")
abstract class AbstractWindow(
    override var x: Int,
    override var y: Int,
    override var width: Int
) : ParentElement {
    override val elements = mutableListOf<AbstractChildElement>()
    override val visible = true
    override val offsets = ElementOffsets.DEFAULT

    private var dragging = false
    private var dragX = 0
    private var dragY = 0

    private var opened = true

    protected val windowElements = object : ObservableCollection.List<AbstractChildElement>() {
        override fun elementAdded(element: AbstractChildElement) {
            if (opened) elements.add(element)
        }

        override fun elementRemoved(element: AbstractChildElement) {
            if (opened) elements.remove(element)
        }
    }

    override var height = 0; get() = visibleElements.sumOf { it.height }

    protected var header: AbstractChildElement? = null
        set(value) {
            if (field != null) {
                elements.removeAt(0)
            }

            field = value

            if (value != null) {
                elements.add(0, value)
            }
        }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        if (dragging) {
            x = mouseX - dragX
            y = mouseY - dragY
        }

        drawRect(context)

        super.render(context, mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == 0 && header?.isHover(mouseX, mouseY) == true) {
            dragging = true
            dragX = mouseX - x
            dragY = mouseY - y
        }

        if (header?.isHover(mouseX, mouseY) == true && button == 1) {
            if (opened) {
                elements.removeIf { windowElements.contains(it) }
            } else {
                elements.addAll(windowElements)
            }

            opened = !opened
        }

        super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging = false

        super.mouseReleased(mouseX, mouseY, state)
    }

    private fun ParentElement.drawRect(context: DrawContext) {
        for (element in this.elements.filter { it.visible }) {
            if (element is ParentElement) {
                element.drawRect(context)
            } else if (element.renderRect) {
                context.drawRect(element.x, element.y, element.width, element.height, AbstractChildElement.rectColor)
            }
        }
    }

    class HeaderElement(
        private val header: String,
        parent: ParentElement
    ) : AbstractChildElement(ClickGUI.ELEMENT_HEIGHT, parent) {
        override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
            super.render(context, mouseX, mouseY)

            context {
                this@HeaderElement.also {
                    drawHorizontalLine(it.x, it.x + it.width, it.y + it.height - 1, mainColor)
                    drawCenteredString(
                        it,
                        header,
                        Color.WHITE
                    )
                }
            }
        }
    }
}