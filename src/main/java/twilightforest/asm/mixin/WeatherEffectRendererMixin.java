package twilightforest.asm.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.init.TFDimension;

@Mixin(ClientLevel.class)
public class WeatherEffectRendererMixin {

	@Inject(
		method = "tickWeatherEffects()V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$customRainTick(
		CallbackInfo ci
	) {
		ClientLevel level = (ClientLevel) (Object) this;
		Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();
		int ticks = (int) level.getGameTime();
		if (level.dimension().equals(TFDimension.DIMENSION_KEY) && TwilightForestRenderInfo.INSTANCE.tickRain(level, ticks, camera)) {
			ci.cancel();
		}
	}
}
