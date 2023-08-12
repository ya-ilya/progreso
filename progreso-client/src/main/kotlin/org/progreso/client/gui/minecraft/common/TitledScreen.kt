package org.progreso.client.gui.minecraft.common

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.progreso.client.accessors.TextAccessor.i18n
import org.progreso.client.gui.drawText
import java.awt.Color

open class TitledScreen(title: String) : Screen(Text.of(title)) {
    @Suppress("UNUSED_PARAMETER")
    constructor(title: String = "", i18n: String) : this(i18n(i18n))

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackgroundTexture(context)
        super.render(context, mouseX, mouseY, delta)
        context.drawText(
            textRenderer,
            title.string,
            width / 2 - client!!.textRenderer.getWidth(title) / 2,
            8,
            Color.WHITE
        )
    }
}