package org.progreso.client.util.render

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier

object Render2DShaderPipelines {
    val ELLIPSE_PIPELINE: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET)
                .withBlend(BlendFunction.TRANSLUCENT)
                .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
                .withCull(true)
                .withFragmentShader(Identifier.of("progreso", "core/rendertype_ellipse"))
                .withVertexShader("core/position_tex_color")
                .withLocation(Identifier.of("progreso", "pipeline/2d/quad_ellipse"))
                .build()
        )

    val PICKER_PIPELINE: RenderPipeline = RenderPipelines
        .register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                .withLocation(Identifier.of("progreso", "pipeline/2d/quad_picker"))
                .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                .build()
        )
}