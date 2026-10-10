package twilightforest.loot;

import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

import java.util.Set;

public record MultiplayerBasedNumberProvider(ContextFloatProvider rollsPerPlayer, ContextFloatProvider defaultRolls) implements ContextFloatProvider {
	public static final MapCodec<MultiplayerBasedNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextFloatProviders.DIRECT_CODEC.fieldOf("per_player_rolls").forGetter(MultiplayerBasedNumberProvider::rollsPerPlayer),
			ContextFloatProviders.DIRECT_CODEC.fieldOf("default_rolls").forGetter(MultiplayerBasedNumberProvider::defaultRolls))
		.apply(instance, MultiplayerBasedNumberProvider::new)
	);

	@Override
	public MapCodec<? extends ContextFloatProvider> codec() {
		return CODEC;
	}

	public static MultiplayerBasedNumberProvider rollsForPlayers(ContextFloatProvider rollsPerPlayer, ContextFloatProvider defaultRolls) {
		return new MultiplayerBasedNumberProvider(rollsPerPlayer, defaultRolls);
	}

	@Override
	public float getFloatUnsafe(LootContext context) {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()) {
			if (context.hasParameter(LootContextParams.THIS_ENTITY) && context.getOptional(LootContextParams.THIS_ENTITY).hasAttached(TFDataAttachments.MULTIPLAYER_FIGHT)) {
				int qualifiedPlayers = context.getOptional(LootContextParams.THIS_ENTITY).getAttached(TFDataAttachments.MULTIPLAYER_FIGHT).getQualifiedPlayers().size();
				float total = this.defaultRolls.getFloat(context);
				for (int i = 0; i < qualifiedPlayers - 1; i++) {
					total += Math.max(0, this.rollsPerPlayer.getFloat(context));
				}
				return total;
			}
		}
		return this.defaultRolls.getFloat(context);

	}

	/**
	 * Get the parameters used by this object.
	 */
	@Override
	public void validate(net.minecraft.world.level.storage.loot.ValidationContext context) {
		this.rollsPerPlayer.validate(context.forField("per_player_rolls"));
		this.defaultRolls.validate(context.forField("default_rolls"));
	}
}
