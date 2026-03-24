package org.progreso.client.util.level

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.chunk.LevelChunk
import org.progreso.client.Client.Companion.mc
import java.util.stream.Stream
import kotlin.math.max

val Level.blockEntities: List<BlockEntity>
    get() {
        val entities = mutableListOf<BlockEntity>()
        loadedChunks.forEach { chunk ->
            entities.addAll(chunk.blockEntities.values)
        }
        return entities
    }

val Level.loadedChunks: Stream<LevelChunk>
    get() {
        val viewDistance = mc.options.renderDistance.get()
        val radius = max(2, viewDistance) + 1

        val center = mc.player.chunkPosition()

        val minX = center.x - radius
        val maxX = center.x + radius
        val minZ = center.z - radius
        val maxZ = center.z + radius

        val chunks = mutableListOf<LevelChunk>()

        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                val chunk = mc.level.chunkSource.getChunk(x, z, false)
                if (chunk is LevelChunk) {
                    chunks.add(chunk)
                }
            }
        }
        return chunks.stream()
    }

fun Level.getBlocksInRadius(radius: Int, pos: BlockPos): List<BlockPos> {
    return getBlocksInRadius(radius, radius, radius, pos)
}

@Suppress("DEPRECATION")
fun Level.getBlocksInRadius(xRadius: Int, yRadius: Int, zRadius: Int, pos: BlockPos): List<BlockPos> {
    val poses = mutableListOf<BlockPos>()

    for (x in -xRadius..xRadius) {
        for (z in -zRadius..zRadius) {
            for (y in -yRadius..yRadius) {
                val blockPos = pos.offset(x, y, z)

                if (hasChunkAt(blockPos)) {
                    poses.add(blockPos)
                }
            }
        }
    }

    return poses
}