package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record BerryBushConfig(BlockState bushState, TagKey<Block> placesOn, boolean canBeSnowy) {

	public static final MapCodec<BerryBushConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.fieldOf("bush").forGetter(BerryBushConfig::bushState),
		TagKey.codec(Registries.BLOCK).fieldOf("generates_on").forGetter(BerryBushConfig::placesOn),
		Codec.BOOL.fieldOf("can_generate_snowy").forGetter(BerryBushConfig::canBeSnowy)
	).apply(instance, BerryBushConfig::new));
	public static final Codec<BerryBushConfig> CODEC = MAP_CODEC.codec();
}
