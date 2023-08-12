package org.progreso.client.gui.builders

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import org.progreso.api.gui.builders.AbstractTextFieldBuilder
import org.progreso.client.Client.Companion.mc

class TextFieldBuilder : AbstractTextFieldBuilder<DrawContext, TextFieldWidget>() {
    companion object {
        fun Screen.textField(text: String = "", block: (TextFieldBuilder) -> Unit): TextFieldWidget {
            return addDrawableChild(
                TextFieldBuilder().apply {
                    if (text.isNotEmpty()) {
                        this.text = text
                    }
                }.apply(block).build()
            )
        }
    }

    override fun build(): TextFieldWidget {
        return object : TextFieldWidget(mc.textRenderer, x, y, width, height, Text.of(text)) {
            init {
                listeners.init(this)
            }

            override fun charTyped(chr: Char, modifiers: Int): Boolean {
                return super.charTyped(chr, modifiers).also {
                    textFieldListeners.textChanged(this)
                }
            }

            override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
                return super.keyPressed(keyCode, scanCode, modifiers).also {
                    textFieldListeners.textChanged(this)
                }
            }

            override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, context, mouseX, mouseY)

                super.render(context, mouseX, mouseY, delta)
            }

            override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
                listeners.mouseClicked(this, mouseX.toInt(), mouseY.toInt(), button)

                return super.mouseClicked(mouseX, mouseY, button)
            }

            override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
                listeners.mouseReleased(this, mouseX.toInt(), mouseY.toInt(), button)

                return super.mouseReleased(mouseX, mouseY, button)
            }
        }
    }
}