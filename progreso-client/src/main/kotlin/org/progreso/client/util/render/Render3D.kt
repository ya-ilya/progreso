package org.progreso.client.util.render

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Camera
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.glColors
import java.awt.Color

data class Render3DContext(val matrices: PoseStack, val camera: Camera? = null)

fun render3D(matrices: PoseStack, block: Render3DContext.() -> Unit) {
    val camera = mc.gameRenderer.mainCamera

    matrices.pushPose()
    block(Render3DContext(matrices, camera))
    matrices.popPose()
}

fun Render3DContext.withPosition(pos: Vec3, block: Render3DContext.() -> Unit) {
    matrices.pushPose()
    matrices.translate(pos.x, pos.y, pos.z)
    block()
    matrices.popPose()
}

fun Render3DContext.withPosition(pos: BlockPos, block: Render3DContext.() -> Unit) {
    withPosition(Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()), block)
}

fun Render3DContext.withRelativeToCameraPosition(pos: Vec3, block: Render3DContext.() -> Unit) {
    val relativePos = pos.subtract(camera!!.position())

    matrices.pushPose()
    matrices.translate(relativePos.x, relativePos.y, relativePos.z)
    block()
    matrices.popPose()
}

fun Render3DContext.withRelativeToCameraPosition(pos: BlockPos, block: Render3DContext.() -> Unit) {
    withRelativeToCameraPosition(Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()), block)
}

fun Render3DContext.drawOutlinedBox(box: AABB, color: Color, lineWidth: Float = 2.5F) {
    val (red, green, blue, alpha) = color.glColors
    val matrix = matrices.last().pose()
    val layer = RenderLayers.getLines(false)
    val bufferSource = mc.renderBuffers.bufferSource()
    val buffer = bufferSource.getBuffer(layer)

    val vertices = listOf(
        Vec3(box.minX, box.minY, box.minZ),
        Vec3(box.maxX, box.minY, box.minZ),
        Vec3(box.maxX, box.minY, box.minZ),
        Vec3(box.maxX, box.minY, box.maxZ),
        Vec3(box.maxX, box.minY, box.maxZ),
        Vec3(box.minX, box.minY, box.maxZ),
        Vec3(box.minX, box.minY, box.maxZ),
        Vec3(box.minX, box.minY, box.minZ),
        Vec3(box.minX, box.minY, box.minZ),
        Vec3(box.minX, box.maxY, box.minZ),
        Vec3(box.maxX, box.minY, box.minZ),
        Vec3(box.maxX, box.maxY, box.minZ),
        Vec3(box.maxX, box.minY, box.maxZ),
        Vec3(box.maxX, box.maxY, box.maxZ),
        Vec3(box.minX, box.minY, box.maxZ),
        Vec3(box.minX, box.maxY, box.maxZ),
        Vec3(box.minX, box.maxY, box.minZ),
        Vec3(box.maxX, box.maxY, box.minZ),
        Vec3(box.maxX, box.maxY, box.minZ),
        Vec3(box.maxX, box.maxY, box.maxZ),
        Vec3(box.maxX, box.maxY, box.maxZ),
        Vec3(box.minX, box.maxY, box.maxZ),
        Vec3(box.minX, box.maxY, box.maxZ),
        Vec3(box.minX, box.maxY, box.minZ)
    )

    val normals = mutableListOf<Vec3>()
    for (i in 0 until vertices.size - 1 step 2) {
        val start = vertices[i]
        val end = vertices[i + 1]
        val direction = end.subtract(start).normalize()
        normals.add(direction)
        normals.add(direction)
    }

    vertices.zip(normals).forEach { (vec3, normal) ->
        buffer
            .addVertex(matrix, vec3.x.toFloat(), vec3.y.toFloat(), vec3.z.toFloat())
            .setLineWidth(lineWidth)
            .setColor(red, green, blue, alpha)
            .setNormal(matrices.last(), normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
    }

    bufferSource.endBatch(layer)
}

fun Render3DContext.drawSolidBox(box: AABB, color: Color, lineWidth: Float = 2.5F) {
    val (red, green, blue, alpha) = color.glColors
    val matrix = matrices.last().pose()
    val layer = RenderLayers.getQuads(false)
    val bufferSource = mc.renderBuffers.bufferSource()
    val buffer = bufferSource.getBuffer(layer)

    val vertices = listOf(
        Vec3(box.minX, box.minY, box.minZ),
        Vec3(box.maxX, box.minY, box.minZ),
        Vec3(box.maxX, box.minY, box.maxZ),
        Vec3(box.minX, box.minY, box.maxZ),
        Vec3(box.minX, box.maxY, box.minZ),
        Vec3(box.minX, box.maxY, box.maxZ),
        Vec3(box.maxX, box.maxY, box.maxZ),
        Vec3(box.maxX, box.maxY, box.minZ),
        Vec3(box.minX, box.minY, box.minZ),
        Vec3(box.minX, box.maxY, box.minZ),
        Vec3(box.maxX, box.maxY, box.minZ),
        Vec3(box.maxX, box.minY, box.minZ),
        Vec3(box.maxX, box.minY, box.minZ),
        Vec3(box.maxX, box.maxY, box.minZ),
        Vec3(box.maxX, box.maxY, box.maxZ),
        Vec3(box.maxX, box.minY, box.maxZ),
        Vec3(box.minX, box.minY, box.maxZ),
        Vec3(box.maxX, box.minY, box.maxZ),
        Vec3(box.maxX, box.maxY, box.maxZ),
        Vec3(box.minX, box.maxY, box.maxZ),
        Vec3(box.minX, box.minY, box.minZ),
        Vec3(box.minX, box.minY, box.maxZ),
        Vec3(box.minX, box.maxY, box.maxZ),
        Vec3(box.minX, box.maxY, box.minZ)
    )

    vertices.forEach { vec3 ->
        buffer
            .addVertex(matrix, vec3.x.toFloat(), vec3.y.toFloat(), vec3.z.toFloat())
            .setLineWidth(lineWidth)
            .setColor(red, green, blue, alpha)
    }

    bufferSource.endBatch(layer)
}