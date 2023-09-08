package org.progreso.client.util.render

import net.minecraft.client.font.FontStorage
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.font.TrueTypeFontLoader
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.progreso.client.Client.Companion.mc
import org.progreso.client.managers.minecraft.ProgresoResourceManager

object TextRendererUtil {
    fun createTextRenderer(
        fontName: String,
        size: Float,
        resourceManager: ResourceManager = mc.resourceManager,
        namespace: String = "progreso"
    ): TextRenderer? {
        val font =
            TrueTypeFontLoader(Identifier(namespace, "$fontName.ttf"), size, 2f, TrueTypeFontLoader.Shift.NONE, "")
                .build()
                .left()

        if (font.isPresent) {
            val fontStorage = FontStorage(mc.client.textureManager, Identifier("progreso"))
            fontStorage.setFonts(listOf(font.get().load(resourceManager)))
            return TextRenderer({ fontStorage }, false)
        }

        return null
    }

    fun createTextRendererFromProgresoResource(
        fontName: String,
        size: Float
    ): TextRenderer? {
        return createTextRenderer(fontName, size, ProgresoResourceManager, "progreso-resources")
    }
}