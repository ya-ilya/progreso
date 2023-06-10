package org.progreso.client.gui.builders

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text
import org.progreso.api.gui.builders.AbstractButtonBuilder
import org.progreso.client.mixins.accessors.AccessorScreen

class ButtonBuilder : AbstractButtonBuilder<DrawContext, ButtonWidget>() {
    companion object {
        fun Screen.button(block: (ButtonBuilder) -> Unit): ButtonWidget {
            return (this as AccessorScreen).addDrawableChildInvoker(
                ButtonBuilder().apply(block).build()
            )
        }

        fun Screen.button(text: String, block: (ButtonBuilder) -> Unit): ButtonWidget {
            return (this as AccessorScreen).addDrawableChildInvoker(
                ButtonBuilder().apply { this.text = text }.apply(block).build()
            )
        }
    }

    override fun build(): ButtonWidget {
        return object : ButtonWidget(
            x,
            y,
            width,
            height,
            Text.of(text),
            { buttonListeners.onPress(it) },
            DEFAULT_NARRATION_SUPPLIER
        ) {
            init {
                active = this@ButtonBuilder.active
                listeners.init(this)
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