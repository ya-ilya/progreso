package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.AbstractChildListElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.invoke
import org.progreso.client.gui.textRenderer
import java.awt.Color

class ModuleElement(
    module: AbstractModule,
    height: Int,
    parent: ParentElement
) : AbstractChildListElement(height, parent) {
    companion object {
        fun AbstractSetting<*>.createElement(height: Int, parent: ParentElement): AbstractChildElement {
            return when (this) {
                is BindSetting -> BindElement(this, height, parent)
                is BooleanSetting -> BooleanElement(this, height, parent)
                is ColorSetting -> ColorElement(this, height, parent)
                is EnumSetting<*> -> EnumElement(this, height, parent)
                is GroupSetting -> GroupElement(this, height, parent)
                is NumberSetting<*> -> SliderElement(this, height, parent)
                is StringSetting -> StringElement(this, height, parent)
                else -> throw RuntimeException("Unknown setting type")
            }
        }
    }

    init {
        listElements.addAll(
            module.settings.map { it.createElement(height, this) }
        )

        header = object : AbstractChildElement(height, this@ModuleElement) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) = context {
                if (module.enabled) {
                    drawTextRelatively(
                        module.name,
                        5,
                        Color.WHITE
                    )
                } else {
                    drawTextRelatively(
                        module.name,
                        5,
                        Color(180, 180, 180)
                    )
                }
            }

            override fun postRender(context: DrawContext, mouseX: Int, mouseY: Int) = context {
                if (descriptions && isHover(mouseX, mouseY) && module.description.isNotBlank()) {
                    val lines = textRenderer.wrapLines(Text.of(module.description), 200)

                    drawBorderedRect(
                        mouseX + 6,
                        mouseY,
                        lines.maxOf { textRenderer.getWidth(it) } + 4,
                        (fontHeight + 3) * lines.size,
                        Color(rectColor.red, rectColor.green, rectColor.blue, 255),
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
            }

            override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
                super.mouseClicked(mouseX, mouseY, button)

                if (button == 0) {
                    module.toggle()
                }
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