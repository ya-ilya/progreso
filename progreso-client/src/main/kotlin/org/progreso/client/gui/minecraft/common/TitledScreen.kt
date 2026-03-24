package org.progreso.client.gui.minecraft.common

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.progreso.client.accessors.TextAccessor.i18n
import org.progreso.client.gui.drawText
import java.awt.Color

open class TitledScreen(title: String) : Screen(Component.literal(title)) {

    constructor(title: String = "", i18n: String) : this(i18n(i18n))

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        super.extractRenderState(graphics, mouseX, mouseY, delta)

        graphics.drawText(
            font,
            title.string,
            width / 2 - font.width(title) / 2,
            8,
            Color.WHITE
        )
    }
}