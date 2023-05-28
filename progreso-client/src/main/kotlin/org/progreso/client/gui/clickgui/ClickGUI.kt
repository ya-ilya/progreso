package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.components.CategoryComponent
import org.progreso.client.module.Category
import org.progreso.client.module.modules.client.ClickGUI
import org.progreso.client.util.render.RenderContext

open class ClickGUI(text: String) : Screen(Text.of(text)) {
    companion object : org.progreso.client.gui.clickgui.ClickGUI("ClickGUI") {
        const val COMPONENT_HEIGHT = 14
        const val COMPONENT_WIDTH = 104
    }

    protected var components = mutableListOf<AbstractComponent>()

    open fun initialize() {
        var x = 10

        for (category in Category.values().filter { it != Category.Hud }) {
            components.add(Window(x, 10, COMPONENT_WIDTH).apply {
                x += COMPONENT_WIDTH + 10

                this.components.add(
                    CategoryComponent(
                        category,
                        COMPONENT_HEIGHT,
                        this
                    )
                )
            })
        }
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(matrices, mouseX, mouseY, delta)

        val context = RenderContext(matrices)
        components.forEach { it.render(context, mouseX, mouseY) }
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (amount > 0) {
            components.forEach { it.y += ClickGUI.scrollSpeed }
        } else if (amount < 0) {
            components.forEach { it.y -= ClickGUI.scrollSpeed }
        }

        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseXInt: Int = mouseX.toInt()
        val mouseYInt: Int = mouseY.toInt()
        val window = components.lastOrNull { it.isHover(mouseXInt, mouseYInt) }

        if (window != null) {
            components.remove(window)
            components.add(window)

            window.mouseClicked(mouseXInt, mouseYInt, button)
        }

        components.filter { it != window }.forEach { it.mouseClickedOutside(mouseXInt, mouseYInt, button) }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseXInt: Int = mouseX.toInt()
        val mouseYInt: Int = mouseY.toInt()

        components.forEach { it.mouseReleased(mouseXInt, mouseYInt, button) }

        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        components.forEach { it.keyPressed(keyCode, scanCode) }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        components.forEach { it.charTyped(chr) }

        return super.charTyped(chr, modifiers)
    }

    override fun shouldPause(): Boolean {
        return false
    }
}