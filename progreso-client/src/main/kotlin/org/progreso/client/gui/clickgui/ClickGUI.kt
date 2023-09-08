package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.progreso.api.module.Category
import org.progreso.client.gui.clickgui.window.AbstractWindow
import org.progreso.client.gui.clickgui.window.windows.CategoryWindow
import org.progreso.client.gui.clickgui.window.windows.DescriptionWindow

open class ClickGUI(title: String) : Screen(Text.of(title)) {
    companion object : ClickGUI("ClickGUI") {
        const val ELEMENT_WIDTH = 104
        const val ELEMENT_HEIGHT = 16
        const val X_INDENT = 10
        const val Y_INDENT = 10

        const val DESCRIPTION_WINDOW_WIDTH = 200
    }

    protected var windows = mutableListOf<AbstractWindow>()

    lateinit var descriptionWindow: DescriptionWindow

    open fun initialize() {
        var x = X_INDENT

        for (category in Category.entries.filter { it != Category.Hud }) {
            windows.add(CategoryWindow(category, x, Y_INDENT, ELEMENT_WIDTH))
            x += ELEMENT_WIDTH + X_INDENT
        }

        windows.add(DescriptionWindow(x, Y_INDENT, DESCRIPTION_WINDOW_WIDTH).also {
            descriptionWindow = it
        })
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        windows.forEach { it.render(context, mouseX, mouseY) }
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        val scrollSpeed = org.progreso.client.modules.client.ClickGUI.scrollSpeed

        if (amount > 0) {
            windows.forEach { it.y += scrollSpeed }
        } else if (amount < 0) {
            windows.forEach { it.y -= scrollSpeed }
        }

        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseXInt: Int = mouseX.toInt()
        val mouseYInt: Int = mouseY.toInt()
        val window = windows.lastOrNull { it.isHover(mouseXInt, mouseYInt) }

        if (window != null) {
            windows.remove(window)
            windows.add(window)

            window.mouseClicked(mouseXInt, mouseYInt, button)
        }

        windows.filter { it != window }.forEach { it.mouseClickedOutside(mouseXInt, mouseYInt, button) }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseXInt: Int = mouseX.toInt()
        val mouseYInt: Int = mouseY.toInt()

        windows.forEach { it.mouseReleased(mouseXInt, mouseYInt, button) }

        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        windows.forEach { it.keyPressed(keyCode, scanCode) }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        windows.forEach { it.charTyped(chr) }

        return super.charTyped(chr, modifiers)
    }

    override fun shouldPause(): Boolean {
        return false
    }
}