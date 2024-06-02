package org.progreso.client.util.world

import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.chunk.WorldChunk
import org.progreso.client.Client
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.math.max

val World.blockEntities: List<BlockEntity>
    get() {
        return loadedChunks
            .flatMap { it.blockEntities.values.stream() }
            .collect(Collectors.toList())
    }

val World.loadedChunks: Stream<WorldChunk>
    get() {
        val radius = max(2, Client.mc.options!!.clampedViewDistance) + 3
        val diameter = radius * 2 + 1
        val center = Client.mc.player.chunkPos
        val min = ChunkPos(center.x - radius, center.z - radius)
        val max = ChunkPos(center.x + radius, center.z + radius)

        return Stream.iterate(min) {
            var x = it.x
            var z = it.z
            x++
            if (x > max.x) {
                x = min.x
                z++
            }
            check(z <= max.z)
            ChunkPos(x, z)
        }
            .limit((diameter * diameter).toLong())
            .filter { isChunkLoaded(it.x, it.z) }
            .map { getChunk(it.x, it.z) }
    }

fun World.getBlocksInRadius(radius: Int, pos: BlockPos): List<BlockPos> {
    return getBlocksInRadius(radius, radius, radius, pos)
}

fun World.getBlocksInRadius(xRadius: Int, yRadius: Int, zRadius: Int, pos: BlockPos): List<BlockPos> {
    val poses = mutableListOf<BlockPos>()

    for (x in -xRadius..xRadius) {
        for (z in -zRadius..zRadius) {
            for (y in -yRadius..yRadius) {
                val blockPos = pos.add(x, y, z)

                if (getBlockState(blockPos) != null) {
                    poses.add(blockPos)
                }
            }
        }
    }

    return poses
}