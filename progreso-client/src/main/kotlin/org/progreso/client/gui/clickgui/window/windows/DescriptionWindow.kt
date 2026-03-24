package org.progreso.client.gui.clickgui.window.windows

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.elements.ModuleElement
import org.progreso.client.gui.clickgui.window.AbstractWindow
import org.progreso.client.gui.font
import java.awt.Color

class DescriptionWindow(x: Int, y: Int, width: Int) : AbstractWindow(x, y, width) {
    private var lastElement: ModuleElement? = null
    private var lines = mutableListOf<FormattedText>()

    init {
        windowElements.add(object : AbstractChildElement(ClickGUI.ELEMENT_HEIGHT, this@DescriptionWindow) {
            override fun render(context: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                for ((index, line) in lines.withIndex()) {
                    context.text(
                        font,
                        line.string,
                        this.x + 2,
                        this.y + 2 + index * (font.lineHeight + 2),
                        Color.WHITE.rgb
                    )
                }
            }
        })

        header = HeaderElement("Description", this)
    }

    fun update(element: ModuleElement? = null) {
        if (element == lastElement) return

        lines = font.splitIgnoringLanguage(
            if (element != null && element.module.description.isNotBlank()) Component.literal(element.module.description)
            else Component.literal("Hover mouse on module"),
            width
        )

        windowElements.first().height = (font.lineHeight + 2) * lines.size + 2
    }
}