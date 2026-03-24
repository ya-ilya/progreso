package org.progreso.client.util.render.elements

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import org.joml.Matrix3x2f
import org.progreso.client.gui.glColors
import java.awt.Color

data class PickerElementRenderState(
    private val pipelineVal: RenderPipeline,
    private val textureSetupVal: TextureSetup,
    val pose: Matrix3x2f,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val color: Color,
    private val scissorAreaVal: ScreenRectangle?,
    private val boundsVal: ScreenRectangle
) : GuiElementRenderState {
    override fun buildVertices(vertices: VertexConsumer) {
        val (red, green, blue, alpha) = color.glColors

        vertices.addVertexWith2DPose(pose, x, y).setColor(1f, 1f, 1f, 1f)
        vertices.addVertexWith2DPose(pose, x, y + height).setColor(1f, 1f, 1f, 1f)
        vertices.addVertexWith2DPose(pose, x + width, y + height).setColor(red, green, blue, alpha)
        vertices.addVertexWith2DPose(pose, x + width, y).setColor(red, green, blue, alpha)

        vertices.addVertexWith2DPose(pose, x, y).setColor(0f, 0f, 0f, 0f)
        vertices.addVertexWith2DPose(pose, x, y + height).setColor(0f, 0f, 0f, 1f)
        vertices.addVertexWith2DPose(pose, x + width, y + height).setColor(0f, 0f, 0f, 1f)
        vertices.addVertexWith2DPose(pose, x + width, y).setColor(0f, 0f, 0f, 0f)
    }

    override fun pipeline(): RenderPipeline = pipelineVal

    override fun textureSetup(): TextureSetup = textureSetupVal

    override fun scissorArea(): ScreenRectangle? = scissorAreaVal

    override fun bounds(): ScreenRectangle = boundsVal
}