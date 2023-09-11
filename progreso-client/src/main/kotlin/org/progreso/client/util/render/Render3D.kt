package org.progreso.client.util.render

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.progreso.client.Client.Companion.mc
import java.awt.Color

data class Render3DContext(val matrices: MatrixStack)

fun render3D(matrices: MatrixStack, block: Render3DContext.() -> Unit) {
    val camera = mc.client.entityRenderDispatcher.camera ?: return
    val cameraPosition = camera.pos

    RenderSystem.enableBlend()
    RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
    RenderSystem.disableDepthTest()

    matrices.push()

    matrices.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z)

    block(Render3DContext(matrices))

    matrices.pop()

    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    RenderSystem.disableBlend()
    RenderSystem.enableDepthTest()
}

fun Render3DContext.withPosition(pos: Vec3d, block: Render3DContext.() -> Unit) {
    matrices.push()
    matrices.translate(pos.x, pos.y, pos.z)
    block()
    matrices.pop()
}

fun Render3DContext.withPosition(pos: BlockPos, block: Render3DContext.() -> Unit) {
    withPosition(Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()), block)
}

fun Render3DContext.withColor(color: Color, block: Render3DContext.() -> Unit) {
    RenderSystem.setShaderColor(
        color.red / 255f,
        color.green / 255f,
        color.blue / 255f,
        color.alpha / 255f
    )
    block()
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
}

fun Render3DContext.drawOutlinedBox(box: Box) {
    val matrix = matrices.peek().positionMatrix
    val buffer = Tessellator.getInstance().buffer

    RenderSystem.setShader(GameRenderer::getPositionProgram)

    buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION)

    val vertices = listOf(
        Vec3d(box.minX, box.minY, box.minZ),
        Vec3d(box.maxX, box.minY, box.minZ),
        Vec3d(box.maxX, box.minY, box.minZ),
        Vec3d(box.maxX, box.minY, box.maxZ),
        Vec3d(box.maxX, box.minY, box.maxZ),
        Vec3d(box.minX, box.minY, box.maxZ),
        Vec3d(box.minX, box.minY, box.maxZ),
        Vec3d(box.minX, box.minY, box.minZ),
        Vec3d(box.minX, box.minY, box.minZ),
        Vec3d(box.minX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.minY, box.minZ),
        Vec3d(box.maxX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.minY, box.maxZ),
        Vec3d(box.maxX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.minY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.maxY, box.maxZ),
        Vec3d(box.maxX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.minZ)
    )

    vertices.forEach { vec3d ->
        buffer.vertex(
            matrix,
            vec3d.x.toFloat(),
            vec3d.y.toFloat(),
            vec3d.z.toFloat()
        ).next()
    }

    Tessellator.getInstance().draw()
}

fun Render3DContext.drawSolidBox(box: Box) {
    val matrix = matrices.peek().positionMatrix
    val buffer = Tessellator.getInstance().buffer

    RenderSystem.setShader { GameRenderer.getPositionProgram() }

    buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION)

    val vertices = listOf(
        Vec3d(box.minX, box.minY, box.minZ),
        Vec3d(box.maxX, box.minY, box.minZ),
        Vec3d(box.maxX, box.minY, box.maxZ),
        Vec3d(box.minX, box.minY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.minZ),
        Vec3d(box.minX, box.maxY, box.maxZ),
        Vec3d(box.maxX, box.maxY, box.maxZ),
        Vec3d(box.maxX, box.maxY, box.minZ),
        Vec3d(box.minX, box.minY, box.minZ),
        Vec3d(box.minX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.minY, box.minZ),
        Vec3d(box.maxX, box.minY, box.minZ),
        Vec3d(box.maxX, box.maxY, box.minZ),
        Vec3d(box.maxX, box.maxY, box.maxZ),
        Vec3d(box.maxX, box.minY, box.maxZ),
        Vec3d(box.minX, box.minY, box.maxZ),
        Vec3d(box.maxX, box.minY, box.maxZ),
        Vec3d(box.maxX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.minY, box.minZ),
        Vec3d(box.minX, box.minY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.maxZ),
        Vec3d(box.minX, box.maxY, box.minZ)
    )

    vertices.forEach { vec3d ->
        buffer.vertex(
            matrix,
            vec3d.x.toFloat(),
            vec3d.y.toFloat(),
            vec3d.z.toFloat()
        ).next()
    }

    Tessellator.getInstance().draw()
}