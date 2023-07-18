package org.progreso.client.gui.clickgui.element

import org.progreso.client.gui.clickgui.element.data.ElementOffsets
import org.progreso.client.modules.client.ClickGUI

@Suppress("SuspiciousVarProperty")
abstract class AbstractChildElement(
    override var height: Int,
    val parent: ParentElement
) : Element {
    companion object {
        val mainColor get() = ClickGUI.mainColor
        val rectColor get() = ClickGUI.rectColor
        val descriptions get() = ClickGUI.descriptions
    }

    open var renderRect = true

    override var x = 0; get() = parent.x
    override var y = 0; get() = parent.getChildElementY(this)
    override var width = 0; get() = parent.width
    override val offsets = ElementOffsets(parent.offsets.childTextOffset)
    override val visible = true
}