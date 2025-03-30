package org.progreso.client.util.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import net.minecraft.client.gl.RenderPipelines

object Render3DShaderPipelines {
    val ESP_LINES: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.RENDERTYPE_LINES_SNIPPET)
            .withLocation("pipeline/progreso_esp_lines")
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
    )

    val QUADS: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                .withLocation("pipeline/progreso_quads")
                .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                .build()
        )

    val ESP_QUADS: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                .withLocation("pipeline/progreso_esp_quads")
                .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
        )
}