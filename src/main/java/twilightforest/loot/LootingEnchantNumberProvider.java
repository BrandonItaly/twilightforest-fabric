package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

import java.util.Set;

public class LootingEnchantNumberProvider implements ContextFloatProvider {
	public static final MapCodec<LootingEnchantNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Enchantment.CODEC.fieldOf("enchantment").forGetter(provider -> provider.enchantment),
			ContextFloatProviders.DIRECT_CODEC.fieldOf("base_value").forGetter(provider -> provider.baseValue))
		.apply(instance, LootingEnchantNumberProvider::new)
	);

	private final Holder<Enchantment> enchantment;
	private final ContextFloatProvider baseValue;

	protected LootingEnchantNumberProvider(Holder<Enchantment> enchantment, ContextFloatProvider baseValue) {
		this.enchantment = enchantment;
		this.baseValue = baseValue;
	}

	@Override
	public MapCodec<? extends ContextFloatProvider> codec() {
		return CODEC;
	}

	public static LootingEnchantNumberProvider applyLootingLevelTo(HolderLookup.Provider provider, ContextFloatProvider baseValue) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		return new LootingEnchantNumberProvider(registrylookup.getOrThrow(Enchantments.LOOTING), baseValue);
	}

	@Override
	public float getFloatUnsafe(LootContext context) {
		if (context.getOptional(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity living) {
			return EnchantmentHelper.getEnchantmentLevel(this.enchantment, living) + this.baseValue.getFloat(context);
		}
		return this.baseValue.getFloat(context);
	}

	@Override
	public void validate(net.minecraft.world.level.storage.loot.ValidationContext context) {
		this.baseValue.validate(context.forField("base_value"));
	}
}
