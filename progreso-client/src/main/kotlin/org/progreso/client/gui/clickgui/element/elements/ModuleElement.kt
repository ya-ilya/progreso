package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import org.progreso.api.module.AbstractModule
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.AbstractChildListElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.clickgui.element.elements.ColorElement.Companion.copy
import org.progreso.client.gui.clickgui.element.elements.SettingElement.Companion.createSettingElement
import org.progreso.client.gui.invoke
import org.progreso.client.gui.textRenderer
import java.awt.Color

class ModuleElement(
    module: AbstractModule,
    height: Int,
    parent: ParentElement
) : AbstractChildListElement(height, parent) {
    private companion object {
        val DISABLED_MODULE_COLOR = Color(180, 180, 180)
    }

    init {
        listElements.addAll(
            module.settings.map { it.createSettingElement(height, this) }
        )

        header = object : AbstractChildElement(height, this@ModuleElement) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) = context {
                drawTextRelatively(
                    module.name,
                    5,
                    if (module.enabled) Color.WHITE else DISABLED_MODULE_COLOR
                )
            }

            override fun postRender(context: DrawContext, mouseX: Int, mouseY: Int) = context {
                if (!descriptions || !isHover(mouseX, mouseY) || module.description.isBlank()) return@context
                val lines = textRenderer.wrapLines(Text.of(module.description), 200)

                drawBorderedRect(
                    mouseX + 6,
                    mouseY,
                    lines.maxOf { textRenderer.getWidth(it) } + 4,
                    (fontHeight + 3) * lines.size,
                    rectColor.copy(255),
                    mainColor
                )

                for ((index, line) in lines.withIndex()) {
                    context.drawText(
                        textRenderer,
                        line,
                        mouseX + 8,
                        mouseY + index * (fontHeight + 3) + 2,
                        mainColor.rgb,
                        false
                    )
                }
            }

            override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
                if (button == 0) module.toggle()
            }
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        if (opened) {
            for (element in visibleElements.drop(1)) {
                context {
                    if (element is AbstractChildListElement) {
                        drawVerticalLine(
                            x,
                            element.y,
                            element.y + (element.header?.height ?: element.height),
                            mainColor
                        )
                    } else {
                        drawVerticalLine(
                            x,
                            element.y,
                            element.y + element.height,
                            mainColor
                        )
                    }
                }
            }
        }
    }
}