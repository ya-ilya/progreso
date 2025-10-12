package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
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
        windows.forEach { it.render(context, mouseX, mouseY) }
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        val scrollSpeed = org.progreso.client.modules.client.ClickGUI.scrollSpeed

        if (verticalAmount > 0) {
            windows.forEach { it.y += scrollSpeed }
        } else if (verticalAmount < 0) {
            windows.forEach { it.y -= scrollSpeed }
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
        val mouseXInt: Int = click.x.toInt()
        val mouseYInt: Int = click.y.toInt()
        val window = windows.lastOrNull { it.isHover(mouseXInt, mouseYInt) }

        if (window != null) {
            windows.remove(window)
            windows.add(window)

            window.mouseClicked(mouseXInt, mouseYInt, click.button())
        }

        windows.filter { it != window }.forEach { it.mouseClickedOutside(mouseXInt, mouseYInt, click.button()) }

        return super.mouseClicked(click, doubled)
    }

    override fun mouseReleased(click: Click): Boolean {
        val mouseXInt: Int = click.x.toInt()
        val mouseYInt: Int = click.y.toInt()

        windows.forEach { it.mouseReleased(mouseXInt, mouseYInt, click.button()) }

        return super.mouseReleased(click)
    }

    override fun keyPressed(input: KeyInput): Boolean {
        windows.forEach { it.keyPressed(input.keycode, input.scancode) }

        return super.keyPressed(input)
    }

    override fun charTyped(input: CharInput): Boolean {
        windows.forEach { it.charTyped(input.codepoint().toChar()) }

        return super.charTyped(input)
    }

    override fun shouldPause(): Boolean {
        return false
    }

    override fun applyBlur(context: DrawContext) {
        // Nothing
    }
}