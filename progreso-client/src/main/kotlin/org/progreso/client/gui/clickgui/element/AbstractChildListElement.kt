package org.progreso.client.gui.clickgui.element

import net.minecraft.client.gui.DrawContext

@Suppress("SuspiciousVarProperty")
abstract class AbstractChildListElement(
    height: Int,
    parent: ParentElement
) : AbstractChildElement(height, parent), ParentElement {
    override val elements = mutableListOf<AbstractChildElement>()

    protected val listElements = mutableListOf<AbstractChildElement>()
    protected var opened = false

    override var height = 0; get() = visibleElements.sumOf { it.height }
    override var renderRect = false

    var header: AbstractChildElement? = null
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
        super<AbstractChildElement>.render(context, mouseX, mouseY)
        super<ParentElement>.render(context, mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (header?.isHover(mouseX, mouseY) == true && button == 1) {
            if (opened) {
                elements.removeIf { listElements.contains(it) }
            } else {
                elements.addAll(listElements)
            }

            opened = !opened
        }

        super<ParentElement>.mouseClicked(mouseX, mouseY, button)
    }
}