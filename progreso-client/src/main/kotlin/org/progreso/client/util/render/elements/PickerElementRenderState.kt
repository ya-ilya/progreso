package org.progreso.client.util.render.elements

import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.gui.ScreenRect
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.texture.TextureSetup
import org.joml.Matrix3x2f
import org.progreso.client.gui.glColors
import java.awt.Color

data class PickerElementRenderState(
    val pipeline: RenderPipeline,
    val textureSetup: TextureSetup,
    val pose: Matrix3x2f,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val color: Color,
    val scissorArea: ScreenRect?,
    val bounds: ScreenRect? = createBounds(
        x.toInt(),
        y.toInt(),
        width.toInt(),
        height.toInt(),
        pose,
        scissorArea
    )
) :
    SimpleGuiElementRenderState {

    override fun setupVertices(vertices: VertexConsumer, depth: Float) {
        val (red, green, blue, alpha) = color.glColors

        vertices.vertex(pose, x, y, depth).color(1f, 1f, 1f, 1f)
        vertices.vertex(pose, x, y + height, depth).color(1f, 1f, 1f, 1f)
        vertices.vertex(pose, x + width, y + height, depth).color(red, green, blue, alpha)
        vertices.vertex(pose, x + width, y, depth).color(red, green, blue, alpha)

        vertices.vertex(pose, x, y, depth).color(0f, 0f, 0f, 0f)
        vertices.vertex(pose, x, y + height, depth).color(0f, 0f, 0f, 1f)
        vertices.vertex(pose, x + width, y + height, depth).color(0f, 0f, 0f, 1f)
        vertices.vertex(pose, x + width, y, depth).color(0f, 0f, 0f, 0f)
    }

    override fun pipeline(): RenderPipeline = pipeline

    override fun textureSetup(): TextureSetup = textureSetup

    override fun scissorArea(): ScreenRect? = null

    override fun bounds(): ScreenRect? = bounds

    companion object {
        private fun createBounds(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            pose: Matrix3x2f,
            scissorArea: ScreenRect?
        ): ScreenRect? {
            val screenRect = ScreenRect(x, y, width, height).transformEachVertex(pose)
            return if (scissorArea != null) scissorArea.intersection(screenRect) else screenRect
        }
    }
}
