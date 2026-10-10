package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asm.hooks.coremod.RenderHooks;

@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererMixin {
    @WrapOperation(
        method = "prepareModel(Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"
        )
    )
    private <S> void twilightforest$renderHeadVisibility(Model<S> model, PoseStack poseStack,
        VertexConsumer buffer, int light, int overlay, int color, Operation<Void> original,
        ModelFeatureRenderer.Submit<S> submit) {
        RenderHooks.renderWithHeadVisibility(submit,
            () -> original.call(model, poseStack, buffer, light, overlay, color));
    }
}
