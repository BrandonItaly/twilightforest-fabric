package twilightforest.asm.hooks.coremod;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public final class RenderHooks {
    public static final RenderStateDataKey<Boolean> HIDE_HEAD_KEY = RenderStateDataKey.create(() -> "hide_head");

    public static void renderWithHeadVisibility(ModelFeatureRenderer.Submit<?> submit, Runnable render) {
        if (!(submit.model() instanceof HeadedModel headed)
            || !(submit.state() instanceof EntityRenderState state)
            || !Boolean.TRUE.equals(state.getData(HIDE_HEAD_KEY))) {
            render.run();
            return;
        }
        boolean wasVisible = headed.getHead().visible;
        headed.getHead().visible = false;
        try {
            render.run();
        } finally {
            headed.getHead().visible = wasVisible;
        }
    }
}
