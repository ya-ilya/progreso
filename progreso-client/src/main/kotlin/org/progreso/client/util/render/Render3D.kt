package org.progreso.client.util.render

import net.minecraft.client.render.Camera
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.glColors
import java.awt.Color

data class Render3DContext(val matrices: MatrixStack, val camera: Camera? = null)

fun render3D(matrices: MatrixStack, block: Render3DContext.() -> Unit) {
    val camera = mc.client.entityRenderDispatcher.camera ?: return

    matrices.push()
    block(Render3DContext(matrices, camera))
    matrices.pop()
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

fun Render3DContext.drawOutlinedBox(box: Box, color: Color) {
    val (red, green, blue, alpha) = color.glColors
    val matrix = matrices.peek().positionMatrix
    val layer = RenderLayers.getLines(false)
    val buffer = vertexConsumerProvider.getBuffer(layer)

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

    val normals = mutableListOf<Vec3d>()
    for (i in 0 until vertices.size - 1 step 2) {
        val start = vertices[i]
        val end = vertices[i + 1]
        val direction = end.subtract(start).normalize()
        normals.add(direction)
        normals.add(direction)
    }

    vertices.zip(normals).forEach { (vec3d, normal) ->
        buffer.vertex(
            matrix,
            vec3d.x.toFloat(),
            vec3d.y.toFloat(),
            vec3d.z.toFloat()
        ).color(red, green, blue, alpha).normal(normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
    }

    vertexConsumerProvider.draw(layer)
}

fun Render3DContext.drawSolidBox(box: Box, color: Color) {
    val (red, green, blue, alpha) = color.glColors
    val matrix = matrices.peek().positionMatrix
    val layer = RenderLayers.getQuads(false)
    val buffer = vertexConsumerProvider.getBuffer(layer)

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
        ).color(red, green, blue, alpha)
    }

    vertexConsumerProvider.draw(layer)
}