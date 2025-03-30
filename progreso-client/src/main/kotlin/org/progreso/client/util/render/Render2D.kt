package org.progreso.client.util.render

import net.minecraft.client.font.*
import net.minecraft.client.gui.DrawContext
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.progreso.client.Client
import org.progreso.client.gui.glColors
import org.progreso.client.managers.ProgresoResourceManager
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

data class Render2DContext(val context: DrawContext)

fun render2D(context: DrawContext, block: Render2DContext.() -> Unit) {
    context.matrices.push()
    block(Render2DContext(context))
    context.matrices.pop()
}

fun Render2DContext.drawCircle(
    centerX: Int,
    centerY: Int,
    angleFrom: Double,
    angleTo: Double,
    segments: Int,
    radius: Double,
    color: Color
) {
    val (red, green, blue, alpha) = color.glColors
    val matrix = context.matrices.peek().positionMatrix

    val angleStep = Math.toRadians(angleTo - angleFrom) / segments

    val layer = Render2DLayers.getTriangles()
    val buffer = context.vertexConsumers.getBuffer(layer)
    buffer
        .vertex(matrix, centerX.toFloat(), centerY.toFloat(), 0f)
        .color(red, green, blue, alpha)

    for (i in segments downTo 0) {
        val theta = Math.toRadians(angleFrom) + i * angleStep
        buffer.vertex(
            matrix,
            (centerX - cos(theta) * radius).toFloat(),
            (centerY - sin(theta) * radius).toFloat(), 0f
        ).color(red, green, blue, alpha)
    }

    context.vertexConsumers.draw(layer)
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