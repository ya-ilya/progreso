package org.progreso.client.gui.component.components

import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.ChildComponent
import org.progreso.client.util.Render2DUtil
import org.progreso.client.util.Render2DUtil.drawStringRelatively
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
                is EnumSetting<*> -> EnumComponent(this, height, parent)
                is NumberSetting<*> -> SliderComponent(this, height, parent)
                is GroupSetting -> GroupComponent(this, height, parent)
                is ColorSetting -> ColorComponent(this, height, parent)
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
            override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
                super.drawComponent(mouseX, mouseY, partialTicks)

                if (module.enabled) {
                    //drawRoundedRect(x + 1, y + 1, width - 2, height - 2, 5, theme)
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

            override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
                super.mouseClicked(mouseX, mouseY, mouseButton)

                if (mouseButton == 0) {
                    module.toggle()
                }
            }
        }
    }

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawComponent(mouseX, mouseY, partialTicks)

        if (opened) {
            for (i in 1..visibleComponents.lastIndex) {
                val component = visibleComponents[i]

                if (component is ListComponent) {
                    Render2DUtil.drawVerticalLine(
                        x,
                        component.y - if (i == 1) 1 else 0,
                        component.y + (component.header?.height ?: component.height),
                        theme
                    )
                } else {
                    Render2DUtil.drawVerticalLine(
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