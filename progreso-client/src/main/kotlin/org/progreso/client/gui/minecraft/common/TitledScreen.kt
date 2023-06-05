package org.progreso.client.gui.minecraft.common

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

open class TitledScreen(title: String) : Screen(Text.of(title)) {
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackgroundTexture(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawCenteredTextWithShadow(matrices, textRenderer, title, width / 2, 8, 16777215)
    }
}