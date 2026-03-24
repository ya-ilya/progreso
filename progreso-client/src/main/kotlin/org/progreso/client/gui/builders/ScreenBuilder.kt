package org.progreso.client.gui.builders

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import org.progreso.api.gui.builders.AbstractScreenBuilder
import org.progreso.client.accessors.TextAccessor.i18n

class ScreenBuilder : AbstractScreenBuilder<GuiGraphicsExtractor, Screen>() {
    companion object {
        fun screen(title: String = "", i18n: String = "", block: ScreenBuilder.() -> Unit): Screen {
            return ScreenBuilder().apply {
                if (title.isNotEmpty() || i18n.isNotEmpty()) {
                    this.title = title.ifEmpty { i18n(i18n) }
                }
            }.apply(block).build()
        }
    }

    override fun build(): Screen {
        return object : Screen(Component.literal(title)) {
            override fun init() {
                listeners.init(this)
            }

            override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, graphics, mouseX, mouseY, delta)

                super.extractRenderState(graphics, mouseX, mouseY, delta)
            }

            override fun mouseClicked(event: MouseButtonEvent, doubled: Boolean): Boolean {
                listeners.mouseClicked(this, event.x.toInt(), event.y.toInt(), event.button())

                return super.mouseClicked(event, doubled)
            }

            override fun mouseReleased(event: MouseButtonEvent): Boolean {
                listeners.mouseReleased(this, event.x.toInt(), event.y.toInt(), event.button())

                return super.mouseReleased(event)
            }

            override fun onClose() {
                super.onClose()

                screenListeners.onClose(this)
            }
        }
    }
}