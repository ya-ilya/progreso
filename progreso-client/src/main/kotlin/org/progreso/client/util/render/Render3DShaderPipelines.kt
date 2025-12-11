package org.progreso.client.util.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.util.Identifier

object Render3DShaderPipelines {
    val ESP_LINES: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.RENDERTYPE_LINES_SNIPPET)
            .withLocation(Identifier.of("progreso", "pipeline/3d/esp_lines"))
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
    )

    val QUADS: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                .withLocation(Identifier.of("progreso", "pipeline/3d/quads"))
                .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                .build()
        )

    val ESP_QUADS: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                .withLocation(Identifier.of("progreso", "pipeline/3d/esp_quads"))
                .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build()
        )
}