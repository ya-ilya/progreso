package org.progreso.client.gui.clickgui.window.windows

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.CharacterVisitor
import net.minecraft.text.OrderedText
import net.minecraft.text.Style
import net.minecraft.text.Text
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.elements.ModuleElement
import org.progreso.client.gui.clickgui.window.AbstractWindow
import org.progreso.client.gui.textRenderer
import java.awt.Color

class DescriptionWindow(x: Int, y: Int, width: Int) : AbstractWindow(x, y, width) {
    private var lastElement: ModuleElement? = null
    private var lines = mutableListOf<OrderedText>()

    init {
        windowElements.add(object : AbstractChildElement(ClickGUI.ELEMENT_HEIGHT, this@DescriptionWindow) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                for ((index, line) in lines.withIndex()) {
                    val visitor = object : CharacterVisitor {
                        val text = Text.literal("")

                        override fun accept(index: Int, style: Style?, codePoint: Int): Boolean {
                            text.append(codePoint.toChar().toString())
                            return true
                        }
                    }

                    line.accept(visitor)

                    context.drawText(
                        textRenderer,
                        visitor.text,
                        this.x + 2,
                        this.y + 2 + index * (textRenderer.fontHeight + 2),
                        Color.WHITE.rgb,
                        false
                    )
                }
            }
        })

        header = HeaderElement("Description", this)
    }

    fun update(element: ModuleElement? = null) {
        if (element == lastElement) return

        lines = textRenderer.wrapLines(
            if (element != null && element.module.description.isNotBlank()) Text.of(element.module.description)
            else Text.of("Hover mouse on module"),
            width
        )

        windowElements.first().height = (textRenderer.fontHeight + 2) * lines.size + 2
    }
}