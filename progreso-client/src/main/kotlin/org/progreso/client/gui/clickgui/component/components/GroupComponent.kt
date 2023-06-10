package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.GroupSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.gui.clickgui.component.components.ModuleComponent.Companion.createComponent
import org.progreso.client.gui.useSuper
import java.awt.Color

class GroupComponent(
    private val setting: GroupSetting,
    height: Int,
    parent: AbstractComponent
) : ListComponent(height, parent) {
    override val visible get() = setting.visibility()

    init {
        offsets.childTextOffset += 3

        listComponents.addAll(
            setting.value.map { it.createComponent(height, this) }
        )

        header = object : ChildComponent(height, this@GroupComponent) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context.useSuper(this) {
                    drawStringRelatively(
                        setting.name,
                        it.parent.offsets.textOffset,
                        Color.WHITE
                    )
                }
            }
        }
    }
}