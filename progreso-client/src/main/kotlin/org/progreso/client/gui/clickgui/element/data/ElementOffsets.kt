package org.progreso.client.gui.clickgui.element.data

data class ElementOffsets(
    val textOffset: Int = 0,
    var childTextOffset: Int = 0
) {
    companion object {
        val DEFAULT = ElementOffsets(8)
    }

    constructor(textOffset: Int) : this(textOffset, textOffset)
}