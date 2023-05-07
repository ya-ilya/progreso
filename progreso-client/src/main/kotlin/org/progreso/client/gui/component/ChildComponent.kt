package org.progreso.client.gui.component

import org.progreso.client.gui.component.data.ComponentOffsets

open class ChildComponent(
    override var height: Int,
    var parent: AbstractComponent
) : AbstractComponent() {
    override var x = 0; get() = parent.x
    override var y = 0; get() = parent.getComponentY(this)
    override var width = 0; get() = parent.width
    override val offsets = ComponentOffsets(parent.offsets.childTextOffset)
}