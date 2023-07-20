package org.progreso.client.util.render

import net.minecraft.client.font.FontStorage
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.font.TrueTypeFontLoader
import net.minecraft.util.Identifier
import org.progreso.client.Client.Companion.mc

object TextRendererUtil {
    fun createTextRenderer(fontName: String, size: Float): TextRenderer? {
        val font =
            TrueTypeFontLoader(Identifier("progreso", "$fontName.ttf"), size, 4f, TrueTypeFontLoader.Shift.NONE, "")
                .build()
                .left()

        if (font.isPresent) {
            val fontStorage = FontStorage(mc.client.textureManager, Identifier("progreso"))
            fontStorage.setFonts(listOf(font.get().load(mc.client.resourceManager)))
            return TextRenderer({ fontStorage }, false)
        }

        return null
    }
}