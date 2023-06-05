package org.progreso.api.gui.render

import java.awt.Color

interface IRenderContext {
    fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Color)
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Color)
    fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Color)
    fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color)
    fun drawBorderedRect(x: Int, y: Int, width: Int, height: Int, fillColor: Color, borderColor: Color)
    fun drawString(text: Any, x: Int, y: Int, color: Color)
}