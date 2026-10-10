package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record RootConfig(BlockStateProvider blockRoot, BlockStateProvider oreRoot) {
	public static final MapCodec<RootConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.DIRECT_CODEC.fieldOf("root_block").forGetter(RootConfig::blockRoot),
		BlockStateProvider.DIRECT_CODEC.fieldOf("root_ore").forGetter(RootConfig::oreRoot)
	).apply(instance, RootConfig::new));
	public static final Codec<RootConfig> CODEC = MAP_CODEC.codec();
}
