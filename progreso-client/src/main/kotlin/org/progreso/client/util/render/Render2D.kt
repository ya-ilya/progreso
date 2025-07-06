package org.progreso.client.util.render

import net.minecraft.client.font.*
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.texture.TextureSetup
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.joml.Matrix3x2f
import org.progreso.client.Client
import org.progreso.client.managers.ProgresoResourceManager
import org.progreso.client.util.render.elements.EllipseElementRenderState
import org.progreso.client.util.render.elements.PickerElementRenderState
import java.awt.Color

data class Render2DContext(val context: DrawContext)

fun render2D(context: DrawContext, block: Render2DContext.() -> Unit) {
    context.matrices.pushMatrix()
    block(Render2DContext(context))
    context.matrices.popMatrix()
}

fun Render2DContext.drawEllipse(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    color: Color
) {
    context.state.addSimpleElement(
        EllipseElementRenderState(
            Render2DShaderPipelines.ELLIPSE_PIPELINE,
            TextureSetup.empty(),
            Matrix3x2f(context.matrices),
            x,
            y,
            width,
            height,
            color,
            context.scissorStack.peekLast()
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
    context.state.addSimpleElement(
        PickerElementRenderState(
            Render2DShaderPipelines.PICKER_PIPELINE,
            TextureSetup.empty(),
            Matrix3x2f(context.matrices),
            x,
            y,
            width,
            height,
            color,
            context.scissorStack.peekLast()
        )
    )
}

fun createTextRenderer(
    fontName: String,
    size: Float,
    resourceManager: ResourceManager = Client.mc.resourceManager,
    namespace: String = "progreso"
): TextRenderer? {
    val font =
        TrueTypeFontLoader(Identifier.of(namespace, "$fontName.ttf"), size, 2f, TrueTypeFontLoader.Shift.NONE, "")
            .build()
            .left()

    if (font.isPresent) {
        val fontStorage = FontStorage(Client.mc.client.textureManager, Identifier.of("progreso"))
        fontStorage.setFonts(
            listOf(Font.FontFilterPair(font.get().load(resourceManager), FontFilterType.FilterMap.NO_FILTER)),
            emptySet()
        )
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