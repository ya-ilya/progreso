package org.progreso.client.gui.clickgui.component.data

data class ComponentOffsets(
    val textOffset: Int = 0,
    var childTextOffset: Int = 0
) {
    constructor(textOffset: Int) : this(textOffset, textOffset)
}