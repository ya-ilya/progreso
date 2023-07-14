package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.gui.invoke
import java.awt.Color

class ModuleComponent(
    module: AbstractModule,
    height: Int,
    parent: AbstractComponent
) : ListComponent(height, parent) {
    companion object {
        fun AbstractSetting<*>.createComponent(height: Int, parent: AbstractComponent): AbstractComponent {
            return when (this) {
                is BindSetting -> BindComponent(this, height, parent)
                is BooleanSetting -> BooleanComponent(this, height, parent)
                is ColorSetting -> ColorComponent(this, height, parent)
                is EnumSetting<*> -> EnumComponent(this, height, parent)
                is GroupSetting -> GroupComponent(this, height, parent)
                is NumberSetting<*> -> SliderComponent(this, height, parent)
                is StringSetting -> StringComponent(this, height, parent)
                else -> throw RuntimeException("Unknown setting type")
            }
        }
    }

    init {
        listComponents.addAll(
            module.settings.map { it.createComponent(height, this) }
        )

        header = object : ChildComponent(height, this@ModuleComponent) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context {
                    if (module.enabled) {
                        drawTextRelatively(
                            module.name,
                            4,
                            Color.WHITE
                        )
                    } else {
                        drawTextRelatively(
                            module.name,
                            4,
                            Color(180, 180, 180)
                        )
                    }
                }
            }

            override fun postRender(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.postRender(context, mouseX, mouseY)

                context {
                    if (descriptions && isHover(mouseX, mouseY) && module.description.isNotBlank()) {
                        val lines = mc.textRenderer.wrapLines(Text.of(module.description), 200)

                        drawBorderedRect(
                            mouseX + 6,
                            mouseY,
                            lines.maxOf { mc.textRenderer.getWidth(it) } + 4,
                            (fontHeight + 3) * lines.size,
                            Color(rectColor.red, rectColor.green, rectColor.blue, 255),
                            mainColor
                        )

                        for ((index, line) in lines.withIndex()) {
                            context.drawText(
                                mc.textRenderer,
                                line,
                                mouseX + 8,
                                mouseY + index * (fontHeight + 3) + 2,
                                mainColor.rgb,
                                false
                            )
                        }
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
            for (component in visibleComponents.drop(1)) {
                context {
                    if (component is ListComponent) {
                        drawVerticalLine(
                            x,
                            component.y,
                            component.y + (component.header?.height ?: component.height),
                            mainColor
                        )
                    } else {
                        drawVerticalLine(
                            x,
                            component.y,
                            component.y + component.height,
                            mainColor
                        )
                    }
                }
            }
        }
    }
}