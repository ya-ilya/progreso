package org.progreso.client.gui.component.components

import org.progreso.api.setting.settings.GroupSetting
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.ChildComponent
import org.progreso.client.gui.component.components.ModuleComponent.Companion.createComponent
import org.progreso.client.util.Render2DUtil.drawStringRelatively
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
            override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
                super.drawComponent(mouseX, mouseY, partialTicks)

                drawStringRelatively(
                    setting.name,
                    this.parent.offsets.textOffset,
                    Color.WHITE
                )
            }
        }
    }
}