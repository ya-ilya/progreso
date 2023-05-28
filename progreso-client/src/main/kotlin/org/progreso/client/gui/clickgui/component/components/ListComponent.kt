package org.progreso.client.gui.clickgui.component.components

import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent

open class ListComponent(
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    val listComponents = mutableSetOf<AbstractComponent>()
    var opened = false

    var header: AbstractComponent? = null
        set(value) {
            if (field != null) {
                components.removeAt(0)
            }

            field = value

            if (value != null) {
                components.add(0, value)
            }
        }

    init {
        renderRect = false
    }

    override var height = 0; get() = visibleComponents.sumOf { it.height }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        if (header?.isHover(mouseX, mouseY) == true) {
            if (button == 1) {
                if (opened) {
                    components.removeIf { listComponents.contains(it) }
                } else {
                    components.addAll(listComponents)
                }

                opened = !opened
            }
        }
    }
}