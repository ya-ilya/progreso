package org.progreso.client.util.render

import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.OutputTarget
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import org.progreso.client.util.render.RenderLayers.ESP_LINES
import org.progreso.client.util.render.RenderLayers.ESP_QUADS
import org.progreso.client.util.render.RenderLayers.LINES
import org.progreso.client.util.render.RenderLayers.QUADS

object RenderLayers {
    private val LINES: RenderType = RenderType.create(
        "progreso:lines_3d",
        RenderSetup
            .builder(RenderPipelines.LINES)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
            .createRenderSetup()
    )

    private val ESP_LINES: RenderType = RenderType.create(
        "progreso:esp_lines_3d",
        RenderSetup
            .builder(Render3DShaderPipelines.ESP_LINES)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
            .createRenderSetup()
    )

    private val QUADS: RenderType = RenderType.create(
        "progreso:quads_3d",
        RenderSetup
            .builder(Render3DShaderPipelines.QUADS)
            .createRenderSetup()
    )

    private val ESP_QUADS: RenderType = RenderType.create(
        "progreso:esp_quads_3d",
        RenderSetup
            .builder(Render3DShaderPipelines.ESP_QUADS)
            .createRenderSetup()
    )

    /**
     * Returns either [QUADS] or[ESP_QUADS] depending on the
     * value of `depthTest`.
     */
    fun getQuads(depthTest: Boolean): RenderType {
        return if (depthTest) QUADS else ESP_QUADS
    }

    /**
     * Returns either [LINES] or [ESP_LINES] depending on the
     * value of `depthTest`.
     */
    fun getLines(depthTest: Boolean): RenderType {
        return if (depthTest) LINES else ESP_LINES
    }
}