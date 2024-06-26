package org.progreso.client.gui.builders

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.progreso.api.gui.builders.AbstractScreenBuilder
import org.progreso.client.accessors.TextAccessor.i18n

class ScreenBuilder : AbstractScreenBuilder<DrawContext, Screen>() {
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
        return object : Screen(Text.of(title)) {
            override fun init() {
                listeners.init(this)
            }

            override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                listeners.render(this, context, mouseX, mouseY, delta)

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

            override fun close() {
                super.close()

                screenListeners.close(this)
            }
        }
    }
}