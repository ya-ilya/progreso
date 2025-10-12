package org.progreso.client.gui.builders

import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
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

            override fun charTyped(input: CharInput): Boolean {
                return super.charTyped(input).also {
                    textFieldListeners.textChanged(this)
                }
            }

            override fun keyPressed(input: KeyInput?): Boolean {
                return super.keyPressed(input).also {
                    textFieldListeners.textChanged(this)
                }
            }

            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, context, mouseX, mouseY, delta)

                super.renderWidget(context, mouseX, mouseY, delta)
            }

            override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
                listeners.mouseClicked(this, click.x.toInt(), click.y.toInt(), click.button())

                return super.mouseClicked(click, doubled)
            }

            override fun mouseReleased(click: Click): Boolean {
                listeners.mouseReleased(this, click.x.toInt(), click.y.toInt(), click.button())

                return super.mouseReleased(click)
            }
        }
    }
}