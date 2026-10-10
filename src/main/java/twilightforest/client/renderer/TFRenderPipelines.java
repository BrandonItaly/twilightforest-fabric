package twilightforest.client.renderer;

import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.BlendFactor;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import twilightforest.TFCommon;

import java.util.Optional;

public class TFRenderPipelines {

	public static final String AURORA_UNIFORM = "TwilightForestAurora";

	public static final BindGroupLayout AURORA_LAYOUT = BindGroupLayout.builder().withUniform(AURORA_UNIFORM, UniformType.UNIFORM_BUFFER).build();

	private static final BlendFunction SHADOW = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

	public static final RenderPipeline AURORA = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
		.withLocation(TFCommon.prefix("pipeline/aurora"))
		.withVertexShader(TFCommon.prefix("core/aurora/aurora"))
		.withFragmentShader(TFCommon.prefix("core/aurora/aurora"))
		.withBindGroupLayout(AURORA_LAYOUT)
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR).withPrimitiveTopology(com.mojang.renderpearl.api.pipeline.PrimitiveTopology.QUADS)
		.withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
		.build();

	public static final RenderPipeline RED_THREAD = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation(TFCommon.prefix("core/red_thread/red_thread"))
		.withBindGroupLayout(BindGroupLayouts.SAMPLER0)
		.withBindGroupLayout(BindGroupLayouts.SAMPLER2)
		.withVertexShader("core/block")
		.withFragmentShader("core/block")
		.withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
		.withVertexBinding(0, DefaultVertexFormat.BLOCK).withPrimitiveTopology(com.mojang.renderpearl.api.pipeline.PrimitiveTopology.QUADS)
		.withCull(true)
		.withDepthStencilState(Optional.of(new DepthStencilState(CompareOp.ALWAYS_PASS, false)))
		.build();

	public static final RenderPipeline PROTECTION_BOX = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation(TFCommon.prefix("pipeline/protection_box"))
		.withVertexShader("core/entity")
		.withFragmentShader("core/entity")
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withShaderDefine("EMISSIVE")
		.withShaderDefine("NO_OVERLAY")
		.withShaderDefine("NO_CARDINAL_LIGHTING")
		.withShaderDefine("APPLY_TEXTURE_MATRIX")
		.withBindGroupLayout(BindGroupLayouts.SAMPLER0)
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withCull(false)
		.withVertexBinding(0, DefaultVertexFormat.ENTITY).withPrimitiveTopology(com.mojang.renderpearl.api.pipeline.PrimitiveTopology.QUADS)
		.withDepthStencilState(DepthStencilState.DEFAULT)
		.build();

	public static final RenderPipeline SHADOW_CLONE = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation(TFCommon.prefix("pipeline/shadow_clone"))
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withBindGroupLayout(BindGroupLayouts.SAMPLER1)
		.withCull(false)
		.withColorTargetState(new ColorTargetState(SHADOW))
		.build();
}
