package org.progreso.client.util.render

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.CompareOp
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

object Render2DShaderPipelines {
    val ELLIPSE_PIPELINE: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath("progreso", "pipeline/2d/quad_ellipse"))
            .withVertexShader("core/position_tex_color")
            .withFragmentShader(Identifier.fromNamespaceAndPath("progreso", "core/rendertype_ellipse"))
            .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(true)
            .build()
    )

    val PICKER_PIPELINE: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath("progreso", "pipeline/2d/quad_picker"))
            .withDepthStencilState(DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .build()
    )
}