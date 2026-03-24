package org.progreso.client.gui.builders

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import org.progreso.api.gui.builders.AbstractTextFieldBuilder
import org.progreso.client.Client.Companion.mc

class TextFieldBuilder : AbstractTextFieldBuilder<GuiGraphicsExtractor, EditBox>() {
    companion object {
        fun Screen.textField(text: String = "", block: (TextFieldBuilder) -> Unit): EditBox {
            return this.addRenderableWidget(
                TextFieldBuilder().apply {
                    if (text.isNotEmpty()) {
                        this.text = text
                    }
                }.apply(block).build()
            )
        }
    }

    override fun build(): EditBox {
        return object : EditBox(mc.font, x, y, width, height, Component.literal(text)) {
            init {
                this.value = this@TextFieldBuilder.text
                listeners.init(this)
            }

            override fun charTyped(event: CharacterEvent): Boolean {
                return super.charTyped(event).also {
                    textFieldListeners.textChanged(this)
                }
            }

            override fun keyPressed(event: KeyEvent): Boolean {
                return super.keyPressed(event).also {
                    textFieldListeners.textChanged(this)
                }
            }

            override fun extractWidgetRenderState(
                graphics: GuiGraphicsExtractor,
                mouseX: Int,
                mouseY: Int,
                delta: Float
            ) {
                listeners.render(this, graphics, mouseX, mouseY, delta)

                super.extractWidgetRenderState(graphics, mouseX, mouseY, delta)
            }

            override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
                listeners.mouseClicked(this, event.x.toInt(), event.y.toInt(), event.button())

                return super.mouseClicked(event, doubleClick)
            }

            override fun mouseReleased(event: MouseButtonEvent): Boolean {
                listeners.mouseReleased(this, event.x.toInt(), event.y.toInt(), event.button())

                return super.mouseReleased(event)
            }
        }
    }
}