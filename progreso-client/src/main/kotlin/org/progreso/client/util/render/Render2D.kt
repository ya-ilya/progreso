package org.progreso.client.util.render

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.*
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.*
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.progreso.client.Client
import org.progreso.client.managers.ProgresoResourceManager
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

data class Render2DContext(val context: DrawContext)

fun render2D(context: DrawContext, block: Render2DContext.() -> Unit) {
    RenderSystem.enableBlend()
    RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
    RenderSystem.disableDepthTest()

    context.matrices.push()

    block(Render2DContext(context))

    context.matrices.pop()

    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    RenderSystem.disableBlend()
    RenderSystem.enableDepthTest()
}

fun Render2DContext.withColor(color: Color, block: Render2DContext.() -> Unit) {
    RenderSystem.setShaderColor(
        color.red / 255f,
        color.green / 255f,
        color.blue / 255f,
        color.alpha / 255f
    )
    block()
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
}

fun Render2DContext.drawCircle(
    centerX: Int,
    centerY: Int,
    angleFrom: Double,
    angleTo: Double,
    segments: Int,
    radius: Double
) {
    val matrix = context.matrices.peek().positionMatrix

    val angleStep = Math.toRadians(angleTo - angleFrom) / segments

    RenderSystem.setShader { GameRenderer.getPositionProgram() }

    val buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION)
    buffer.vertex(matrix, centerX.toFloat(), centerY.toFloat(), 0f)

    for (i in segments downTo 0) {
        val theta = Math.toRadians(angleFrom) + i * angleStep
        buffer.vertex(
            matrix,
            (centerX - cos(theta) * radius).toFloat(),
            (centerY - sin(theta) * radius).toFloat(), 0f
        )
    }

    BufferRenderer.drawWithGlobalProgram(buffer.end())
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