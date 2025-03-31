package org.progreso.client.util.render

import net.minecraft.client.render.RenderLayer

object Render2DLayers {
    private val TRIANGLES: RenderLayer.MultiPhase = RenderLayer.of(
        "progreso:triangles_2d",
        1536,
        false,
        true,
        Render2DShaderPipelines.TRIANGLES,
        RenderLayer.MultiPhaseParameters.builder().build(false)
    )

    private val QUADS: RenderLayer.MultiPhase = RenderLayer.of(
        "progreso:quads_2d",
        1536,
        false,
        true,
        Render2DShaderPipelines.QUADS,
        RenderLayer.MultiPhaseParameters.builder().build(false)
    )

    fun getTriangles() = TRIANGLES
    fun getQuads() = QUADS
}