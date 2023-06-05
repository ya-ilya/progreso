package org.progreso.client.gui.builders

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import org.progreso.api.gui.builders.AbstractTextFieldBuilder
import org.progreso.client.Client.Companion.mc
import org.progreso.client.mixins.accessors.AccessorScreen
import org.progreso.client.util.render.RenderContext

class TextFieldBuilder : AbstractTextFieldBuilder<TextFieldWidget>() {
    companion object {
        fun Screen.textField(text: String, block: (TextFieldBuilder) -> Unit): TextFieldWidget {
            return (this as AccessorScreen).addDrawableChildInvoker(
                TextFieldBuilder().apply { this.text = text }.apply(block).build()
            )
        }

        fun Screen.textField(block: (TextFieldBuilder) -> Unit): TextFieldWidget {
            return (this as AccessorScreen).addDrawableChildInvoker(
                TextFieldBuilder().apply(block).build()
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

            override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, RenderContext(matrices), mouseX, mouseY)

                super.render(matrices, mouseX, mouseY, delta)
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