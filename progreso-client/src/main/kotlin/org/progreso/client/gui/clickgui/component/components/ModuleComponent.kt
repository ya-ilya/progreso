package org.progreso.client.gui.clickgui.component.components

import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.settings.*
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.util.Render2DUtil.drawStringRelatively
import org.progreso.client.util.Render2DUtil.drawVerticalLine
import java.awt.Color

class ModuleComponent(
    module: AbstractModule,
    height: Int,
    parent: AbstractComponent
) : ListComponent(height, parent) {
    companion object {
        val MODULE_COMPONENTS = mutableMapOf<AbstractModule, ModuleComponent>()

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

        MODULE_COMPONENTS[module] = this
    }

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawComponent(mouseX, mouseY, partialTicks)

        if (opened) {
            for (i in 1..visibleComponents.lastIndex) {
                val component = visibleComponents[i]

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