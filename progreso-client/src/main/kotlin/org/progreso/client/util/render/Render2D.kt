package org.progreso.client.util.render

import com.mojang.blaze3d.font.GlyphProvider
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.font.FontOption
import net.minecraft.client.gui.font.FontSet
import net.minecraft.client.gui.font.GlyphStitcher
import net.minecraft.client.gui.font.providers.TrueTypeGlyphProviderDefinition
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import org.joml.Matrix3x2f
import org.progreso.client.Client
import org.progreso.client.managers.ProgresoResourceManager
import org.progreso.client.util.render.elements.EllipseElementRenderState
import org.progreso.client.util.render.elements.PickerElementRenderState
import java.awt.Color

data class Render2DContext(val extractor: GuiGraphicsExtractor)

fun render2D(extractor: GuiGraphicsExtractor, block: Render2DContext.() -> Unit) {
    extractor.pose().pushMatrix()
    block(Render2DContext(extractor))
    extractor.pose().popMatrix()
}

fun Render2DContext.drawEllipse(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    color: Color
) {
    val currentPose = Matrix3x2f(extractor.pose())
    val bounds = ScreenRectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt())

    extractor.guiRenderState.addGuiElement(
        EllipseElementRenderState(
            Render2DShaderPipelines.ELLIPSE_PIPELINE,
            TextureSetup.noTexture(),
            currentPose,
            x,
            y,
            width,
            height,
            color,
            extractor.scissorStack.peek(),
            bounds
        )
    )
}

fun Render2DContext.drawPicker(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    color: Color
) {
    val currentPose = Matrix3x2f(extractor.pose())
    val bounds = ScreenRectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt())

    extractor.guiRenderState.addGuiElement(
        PickerElementRenderState(
            Render2DShaderPipelines.PICKER_PIPELINE,
            TextureSetup.noTexture(),
            currentPose,
            x,
            y,
            width,
            height,
            color,
            extractor.scissorStack.peek(),
            bounds
        )
    )
}

fun createTextRenderer(
    fontName: String,
    size: Float,
    resourceManager: ResourceManager = Client.mc.resourceManager,
    namespace: String = "progreso"
): Font? {
    val fontId = Identifier.fromNamespaceAndPath(namespace, fontName.lowercase())
    val fileId = Identifier.fromNamespaceAndPath(namespace, "$fontName.ttf")

    val definition = TrueTypeGlyphProviderDefinition(
        fileId,
        size,
        2.0f,
        TrueTypeGlyphProviderDefinition.Shift(0.0f, 0.0f),
        ""
    )

    try {
        val loader = definition.unpack().left().orElseThrow()
        val provider = loader.load(resourceManager)

        val stitcher = GlyphStitcher(
            Client.mc.client.textureManager,
            fontId
        )

        val fontSet = FontSet(stitcher)
        val conditionalProvider = GlyphProvider.Conditional(provider, FontOption.Filter.ALWAYS_PASS)

        fontSet.reload(listOf(conditionalProvider), emptySet())

        val fontProvider = object : Font.Provider {
            override fun glyphs(fontDescription: net.minecraft.network.chat.FontDescription): net.minecraft.client.gui.GlyphSource {
                return fontSet.source(false)
            }

            override fun effect(): net.minecraft.client.gui.font.glyphs.EffectGlyph {
                return fontSet.whiteGlyph()
            }
        }

        return Font(fontProvider)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun createTextRendererFromProgresoResource(
    fontName: String,
    size: Float
): Font? {
    return createTextRenderer(fontName, size, ProgresoResourceManager, "progreso-resources")
}