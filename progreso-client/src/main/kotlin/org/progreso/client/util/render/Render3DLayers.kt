package org.progreso.client.util.render

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.*
import org.progreso.client.Client.Companion.mc

val vertexConsumerProvider: VertexConsumerProvider.Immediate = mc.bufferBuilders.entityVertexConsumers

object RenderLayers {
    private val LINES: RenderLayer = RenderLayer.of(
        "progreso:lines_3d",
        RenderSetup
            .builder(RenderPipelines.LINES)
            .layeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .outputTarget(OutputTarget.ITEM_ENTITY_TARGET)
            .build()
    )

    private val ESP_LINES: RenderLayer = RenderLayer.of(
        "progreso:esp_lines_3d",
        RenderSetup
            .builder(Render3DShaderPipelines.ESP_LINES)
            .layeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .outputTarget(OutputTarget.ITEM_ENTITY_TARGET)
            .build()
    )

    private val QUADS: RenderLayer = RenderLayer.of(
        "progreso:quads_3d",
        RenderSetup
            .builder(Render3DShaderPipelines.QUADS)
            .build()
    )

    private val ESP_QUADS: RenderLayer = RenderLayer.of(
        "progreso:esp_quads_3d",
        RenderSetup
            .builder(Render3DShaderPipelines.ESP_QUADS)
            .build()
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