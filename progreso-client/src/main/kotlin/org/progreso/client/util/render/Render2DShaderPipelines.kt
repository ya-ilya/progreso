package org.progreso.client.util.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.VertexFormats

object Render2DShaderPipelines {
    val TRIANGLES: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                .withLocation("pipeline/progreso_triangles_2d")
                .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_FAN)
                .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                .build()
        )

    val QUADS: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                .withLocation("pipeline/progreso_quads_2d")
                .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                .build()
        )
}