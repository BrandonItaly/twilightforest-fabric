package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import twilightforest.util.features.FeatureLogic;
import twilightforest.world.components.feature.TFMushroomFeature;

// [VanillaCopy]
public class TrollHugeBrownMushroomFeature extends TFMushroomFeature {
	public static final MapCodec<TrollHugeBrownMushroomFeature> CODEC = settingsCodec(TrollHugeBrownMushroomFeature::new);

	@Override
	public MapCodec<TrollHugeBrownMushroomFeature> codec() { return CODEC; }

	public TrollHugeBrownMushroomFeature(AbstractHugeMushroomFeature config) {
		super(config);
	}

	@Override
	protected void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, AbstractHugeMushroomFeature featureConfiguration) {
		int foliageRadius = featureConfiguration.foliageRadius();

		for (int x = -foliageRadius; x <= foliageRadius; x++) {
			for (int z = -foliageRadius; z <= foliageRadius; z++) {
				if (!FeatureLogic.isCornerInSquare(x, z, foliageRadius)) {
					mutableBlockPos.setWithOffset(pos, x, height, z);

					if (!levelAccessor.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
						BlockState blockState = featureConfiguration.capProvider().value().getState(levelAccessor, random, pos);
						if (FeatureLogic.hasHorizontalMushroomProperties(blockState)) {
							blockState = FeatureLogic.getHorizontalMushroomBlockState(blockState, x, z, foliageRadius);
						}

						this.setBlock(levelAccessor, mutableBlockPos, blockState);
					}
				}
			}
		}
	}
}
