package org.progreso.client.gui.clickgui.element

import net.minecraft.client.gui.DrawContext
import org.progreso.client.gui.clickgui.element.data.ElementOffsets

interface Element {
    var x: Int
    var y: Int
    var width: Int
    var height: Int

    val visible: Boolean
    val offsets: ElementOffsets

    fun render(context: DrawContext, mouseX: Int, mouseY: Int) {}

    fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {}

    fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {}

    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {}

    fun keyPressed(keyCode: Int, scanCode: Int) {}

    fun charTyped(char: Char) {}

    fun isHover(x: Int, y: Int): Boolean {
        return x > this.x && x < this.x + width && y > this.y && y < this.y + height
    }
}