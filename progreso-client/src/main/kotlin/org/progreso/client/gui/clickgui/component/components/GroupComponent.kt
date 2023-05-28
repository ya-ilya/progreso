package org.progreso.client.gui.clickgui.component.components

import org.progreso.api.setting.settings.GroupSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.gui.clickgui.component.components.ModuleComponent.Companion.createComponent
import org.progreso.client.util.render.RenderContext
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
            override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context.invokeSuper(this) {
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