package org.progreso.client.gui.minecraft.common

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.progreso.client.gui.drawText
import java.awt.Color

open class TitledScreen(title: String) : Screen(Text.of(title)) {
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackgroundTexture(context)
        super.render(context, mouseX, mouseY, delta)
        context.drawText(title, width / 2, 8, Color.WHITE, true)
    }
}