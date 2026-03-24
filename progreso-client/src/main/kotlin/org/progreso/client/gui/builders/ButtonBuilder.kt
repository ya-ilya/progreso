package org.progreso.client.gui.builders

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.progreso.api.gui.builders.AbstractButtonBuilder
import org.progreso.client.accessors.TextAccessor.i18n
import java.util.function.Supplier

class ButtonBuilder : AbstractButtonBuilder<GuiGraphicsExtractor, Button>() {
    companion object {
        fun Screen.button(text: String = "", i18n: String = "", block: (ButtonBuilder) -> Unit): Button {
            return this.addRenderableWidget(
                ButtonBuilder().apply {
                    if (text.isNotEmpty() || i18n.isNotEmpty()) {
                        this.text = text.ifEmpty { i18n(i18n) }
                    }
                }.apply(block).build()
            )
        }
    }

    override fun build(): Button {
        return object : Button(
            x,
            y,
            width,
            height,
            Component.literal(text),
            { buttonListeners.onPress(it) },
            CreateNarration { defaultNarrationSupplier: Supplier<MutableComponent> -> defaultNarrationSupplier.get() }
        ) {
            init {
                this.active = this@ButtonBuilder.active
                listeners.init(this)
            }

            override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
                this.extractDefaultSprite(graphics)
                this.extractDefaultLabel(
                    graphics.textRendererForWidget(
                        this,
                        GuiGraphicsExtractor.HoveredTextEffects.NONE
                    )
                )

                listeners.render(this, graphics, mouseX, mouseY, delta)
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