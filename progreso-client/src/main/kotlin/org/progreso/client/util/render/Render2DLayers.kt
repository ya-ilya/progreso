package org.progreso.client.util.render

import net.minecraft.client.render.RenderLayer

object Render2DLayers {
    private val TRIANGLES: RenderLayer.MultiPhase = RenderLayer.of(
        "progreso:triangles",
        1536,
        false,
        true,
        Render2DShaderPipelines.TRIANGLES,
        RenderLayer.MultiPhaseParameters.builder().build(false)
    )

    fun getTriangles() = TRIANGLES
}