package org.progreso.client.util.render

import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import java.util.*

object Render3DShaderPipelines {
    val ESP_LINES: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath("progreso", "pipeline/3d/esp_lines"))
            .withDepthStencilState(Optional.empty())
            .build()
    )

    val QUADS: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath("progreso", "pipeline/3d/quads"))
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .build()
    )

    val ESP_QUADS: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath("progreso", "pipeline/3d/esp_quads"))
            .withDepthStencilState(Optional.empty())
            .build()
    )
}