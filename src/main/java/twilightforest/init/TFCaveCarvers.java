package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import twilightforest.TFCommon;
import twilightforest.world.components.TFCavesCarver;

public final class TFCaveCarvers {
    public static final MapCodec<TFCavesCarver> TF_CAVES = Registry.register(BuiltInRegistries.CARVER_TYPE, TFCommon.prefix("tf_caves"), TFCavesCarver.CODEC);
    public static final MapCodec<TFCavesCarver> HIGHLAND_CAVES = Registry.register(BuiltInRegistries.CARVER_TYPE, TFCommon.prefix("highland_caves"), TFCavesCarver.HIGHLAND_CODEC);
    public static final ResourceKey<WorldCarver> TFCAVES_CONFIGURED = ResourceKey.create(Registries.CARVER, TFCommon.prefix("tf_caves"));
    public static final ResourceKey<WorldCarver> HIGHLANDCAVES_CONFIGURED = ResourceKey.create(Registries.CARVER, TFCommon.prefix("highland_caves"));
    public static void init() { TFCommon.LOGGER.info("Initializing cave carvers..."); }
}