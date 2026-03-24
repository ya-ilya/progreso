package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import org.progreso.api.module.Category
import org.progreso.client.gui.clickgui.window.AbstractWindow
import org.progreso.client.gui.clickgui.window.windows.CategoryWindow
import org.progreso.client.gui.clickgui.window.windows.DescriptionWindow

open class ClickGUI(title: String) : Screen(Component.literal(title)) {
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

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        windows.forEach { it.render(graphics, mouseX, mouseY) }

        super.extractRenderState(graphics, mouseX, mouseY, delta)
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

    override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
        val mouseXInt: Int = event.x.toInt()
        val mouseYInt: Int = event.y.toInt()
        val window = windows.lastOrNull { it.isHover(mouseXInt, mouseYInt) }

        if (window != null) {
            windows.remove(window)
            windows.add(window)

            window.mouseClicked(mouseXInt, mouseYInt, event.button())
        }

        windows.filter { it != window }.forEach { it.mouseClickedOutside(mouseXInt, mouseYInt, event.button()) }

        return super.mouseClicked(event, doubleClick)
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        val mouseXInt: Int = event.x.toInt()
        val mouseYInt: Int = event.y.toInt()

        windows.forEach { it.mouseReleased(mouseXInt, mouseYInt, event.button()) }

        return super.mouseReleased(event)
    }

    override fun keyPressed(event: KeyEvent): Boolean {
        windows.forEach { it.keyPressed(event.key, event.scancode) }

        return super.keyPressed(event)
    }

    override fun charTyped(event: CharacterEvent): Boolean {
        windows.forEach { it.charTyped(event.codepoint().toChar()) }

        return super.charTyped(event)
    }

    override fun isPauseScreen(): Boolean {
        return false
    }

    override fun extractBlurredBackground(graphics: GuiGraphicsExtractor) {
        // Nothing
    }
}