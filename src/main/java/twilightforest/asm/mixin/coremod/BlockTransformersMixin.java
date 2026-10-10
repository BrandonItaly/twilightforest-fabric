package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;

@Mixin(BlockTransformers.class)
public abstract class BlockTransformersMixin {
    @ModifyReturnValue(method = "axeStrippables", at = @At("RETURN"))
    private static BlockTransformer.BlockTransformData twilightforest$addStrippableLogs(BlockTransformer.BlockTransformData original) {
        RuleBasedStateProvider nativeProvider = (RuleBasedStateProvider) original.blockStateProvider().value();
        var rules = new ArrayList<>(nativeProvider.rules());
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.TWILIGHT_OAK_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.CANOPY_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_CANOPY_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.MANGROVE_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_MANGROVE_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.DARK_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_DARK_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.TIME_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_TIME_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.TRANSFORMATION_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_TRANSFORMATION_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.MINING_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_MINING_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.SORTING_LOG), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_SORTING_LOG))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.TWILIGHT_OAK_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.CANOPY_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_CANOPY_WOOD))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.MANGROVE_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_MANGROVE_WOOD))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.DARK_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_DARK_WOOD))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.TIME_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_TIME_WOOD))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.TRANSFORMATION_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_TRANSFORMATION_WOOD))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.MINING_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_MINING_WOOD))));
        rules.add(new RuleBasedStateProvider.Rule(BlockPredicate.matchesBlocks(TFBlocks.SORTING_WOOD), Holder.direct(new CopyPropertiesProvider(TFBlocks.STRIPPED_SORTING_WOOD))));
        var provider = new RuleBasedStateProvider(nativeProvider.fallback(), java.util.List.copyOf(rules));
        return new BlockTransformer.BlockTransformData(Holder.direct(provider), original.sound(), original.particle(),
            original.disallowedFaces(), original.loot(), original.dropStrategy(), original.updateFromNeighbors(),
            original.transformType(), original.consumeOnUse(), original.itemDamagePerUse());
    }
}