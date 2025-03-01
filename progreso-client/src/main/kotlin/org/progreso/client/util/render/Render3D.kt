package org.progreso.client.util.render

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gl.ShaderProgramKeys
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.lwjgl.opengl.GL11C
import org.progreso.client.Client.Companion.mc
import java.awt.Color

data class Render3DContext(val matrices: MatrixStack, val camera: Camera? = null)

fun render3D(matrices: MatrixStack, block: Render3DContext.() -> Unit) {
    val camera = mc.client.entityRenderDispatcher.camera ?: return

    RenderSystem.enableBlend()
    RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
    RenderSystem.disableDepthTest()
    GL11C.glEnable(GL11C.GL_LINE_SMOOTH)

    matrices.push()
    block(Render3DContext(matrices, camera))
    matrices.pop()

    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    RenderSystem.disableBlend()
    RenderSystem.enableDepthTest()
    RenderSystem.enableCull()
    GL11C.glDisable(GL11C.GL_LINE_SMOOTH)
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

fun Render3DContext.withRelativeToCameraPosition(pos: Vec3d, block: Render3DContext.() -> Unit) {
    val relativePos = pos.subtract(camera!!.pos)

    matrices.push()
    matrices.translate(relativePos.x, relativePos.y, relativePos.z)
    block()
    matrices.pop()
}

fun Render3DContext.withRelativeToCameraPosition(pos: BlockPos, block: Render3DContext.() -> Unit) {
    withRelativeToCameraPosition(Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()), block)
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
    val buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION)
    RenderSystem.setShader(ShaderProgramKeys.POSITION)

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
        )
    }

    BufferRenderer.drawWithGlobalProgram(buffer.end())
}

fun Render3DContext.drawSolidBox(box: Box) {
    val matrix = matrices.peek().positionMatrix
    val buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION)
    RenderSystem.setShader(ShaderProgramKeys.POSITION)

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
        )
    }

    BufferRenderer.drawWithGlobalProgram(buffer.end())
}