package org.progreso.client.util.render

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase.LineWidth
import net.minecraft.client.render.VertexConsumerProvider
import org.progreso.client.Client.Companion.mc
import java.util.*

val vertexConsumerProvider: VertexConsumerProvider.Immediate = mc.bufferBuilders.entityVertexConsumers

object RenderLayers {
    private val LINES: RenderLayer.MultiPhase = RenderLayer.of(
        "progreso:lines", 1536, RenderPipelines.LINES,
        RenderLayer.MultiPhaseParameters.builder()
            .lineWidth(LineWidth(OptionalDouble.of(2.0)))
            .layering(RenderLayer.VIEW_OFFSET_Z_LAYERING)
            .target(RenderLayer.ITEM_ENTITY_TARGET).build(false)
    )

    private val ESP_LINES: RenderLayer.MultiPhase = RenderLayer.of(
        "progreso:esp_lines", 1536, Render3DShaderPipelines.ESP_LINES,
        RenderLayer.MultiPhaseParameters.builder()
            .lineWidth(LineWidth(OptionalDouble.of(2.0)))
            .layering(RenderLayer.VIEW_OFFSET_Z_LAYERING)
            .target(RenderLayer.ITEM_ENTITY_TARGET).build(false)
    )

    private val QUADS: RenderLayer.MultiPhase = RenderLayer.of(
        "progreso:quads",
        1536,
        false,
        true,
        Render3DShaderPipelines.QUADS,
        RenderLayer.MultiPhaseParameters.builder().build(false)
    )

    private val ESP_QUADS: RenderLayer.MultiPhase = RenderLayer.of(
        "progreso:esp_quads",
        1536,
        false,
        true,
        Render3DShaderPipelines.ESP_QUADS,
        RenderLayer.MultiPhaseParameters.builder().build(false)
    )

    /**
     * Returns either [.QUADS] or [.ESP_QUADS] depending on the
     * value of `depthTest`.
     */
    fun getQuads(depthTest: Boolean): RenderLayer {
        return if (depthTest) QUADS else ESP_QUADS
    }

    /**
     * Returns either [.LINES] or [.ESP_LINES] depending on the
     * value of `depthTest`.
     */
    fun getLines(depthTest: Boolean): RenderLayer {
        return if (depthTest) LINES else ESP_LINES
    }
}