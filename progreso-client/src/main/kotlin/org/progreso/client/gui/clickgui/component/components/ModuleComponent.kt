package org.progreso.client.gui.clickgui.component.components

import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.util.render.RenderContext
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
            override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context {
                    if (module.enabled) {
                        drawStringRelatively(
                            module.name,
                            4,
                            Color.WHITE
                        )
                    } else {
                        drawStringRelatively(
                            module.name,
                            4,
                            Color(180, 180, 180)
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

    override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        if (opened) {
            for (i in 1..visibleComponents.lastIndex) {
                val component = visibleComponents[i]

                context {
                    if (component is ListComponent) {
                        drawVerticalLine(
                            x,
                            component.y - if (i != 1) 1 else 0,
                            component.y + (component.header?.height ?: component.height),
                            theme
                        )
                    } else {
                        drawVerticalLine(
                            x,
                            component.y,
                            component.y + component.height + if (i != visibleComponents.lastIndex) 1 else 0,
                            theme
                        )
                    }
                }
            }
        }
    }
}