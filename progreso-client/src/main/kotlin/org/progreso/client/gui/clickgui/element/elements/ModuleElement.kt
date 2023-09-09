package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.module.AbstractHudModule
import org.progreso.api.module.AbstractModule
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.AbstractChildListElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.clickgui.element.elements.SettingElement.Companion.createSettingElement
import org.progreso.client.gui.invoke
import java.awt.Color

class ModuleElement(
    val module: AbstractModule,
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

                if (isHover(mouseX, mouseY)) {
                    (if (module is AbstractHudModule) HudEditor else ClickGUI)
                        .descriptionWindow
                        .update(this@ModuleElement)
                }
            }

            override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
                if (button == 0) module.toggle()
            }
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        if (!opened) return

        for (element in visibleElements.drop(1)) context {
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