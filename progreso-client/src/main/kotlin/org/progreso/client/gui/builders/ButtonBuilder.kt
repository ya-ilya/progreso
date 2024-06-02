package org.progreso.client.gui.builders

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text
import org.progreso.api.gui.builders.AbstractButtonBuilder
import org.progreso.client.accessors.TextAccessor.i18n

class ButtonBuilder : AbstractButtonBuilder<DrawContext, ButtonWidget>() {
    companion object {
        fun Screen.button(text: String = "", i18n: String = "", block: (ButtonBuilder) -> Unit): ButtonWidget {
            return addDrawableChild(
                ButtonBuilder().apply {
                    if (text.isNotEmpty() || i18n.isNotEmpty()) {
                        this.text = text.ifEmpty { i18n(i18n) }
                    }
                }.apply(block).build()
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

            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, context, mouseX, mouseY, delta)

                super.renderWidget(context, mouseX, mouseY, delta)
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