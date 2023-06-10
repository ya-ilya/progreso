package org.progreso.client.util.render

import java.awt.Color

object Render2DUtil {
    val Color.glColors: List<Float>
        get() = listOf(
            (rgb shr 16 and 0xFF) / 255.0f,
            (rgb shr 8 and 0xFF) / 255.0f,
            (rgb and 0xFF) / 255.0f,
            (rgb shr 24 and 0xFF) / 255.0f,
        )
}