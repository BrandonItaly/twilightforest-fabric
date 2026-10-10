package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import twilightforest.util.features.FeatureLogic;
import twilightforest.world.components.feature.TFMushroomFeature;

// [VanillaCopy]
public class TrollHugeRedMushroomFeature extends TFMushroomFeature {
	public static final MapCodec<TrollHugeRedMushroomFeature> CODEC = settingsCodec(TrollHugeRedMushroomFeature::new);

	@Override
	public MapCodec<TrollHugeRedMushroomFeature> codec() { return CODEC; }

	public TrollHugeRedMushroomFeature(AbstractHugeMushroomFeature config) {
		super(new HugeRedMushroomFeature(config.capProvider(), config.stemProvider(), config.foliageRadius(), config.canPlaceOn()));
	}

	@Override
	protected void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, AbstractHugeMushroomFeature featureConfiguration) {
		for (int y = height - 3; y <= height; y++) {
			int foliageRadius = y < height ? featureConfiguration.foliageRadius() : featureConfiguration.foliageRadius() - 1;
			int innerRadius = featureConfiguration.foliageRadius() - 2;

			for (int x = -foliageRadius; x <= foliageRadius; x++) {
				for (int z = -foliageRadius; z <= foliageRadius; z++) {
					if (y >= height || FeatureLogic.isEdge(x, z, foliageRadius)) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (!levelAccessor.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
							BlockState blockState = featureConfiguration.capProvider().value().getState(levelAccessor, random, pos);
							if (FeatureLogic.hasAllMushroomsProperties(blockState)) {
								blockState = blockState.setValue(HugeMushroomBlock.UP, y >= height - 1)
									.setValue(HugeMushroomBlock.WEST, x < -innerRadius)
									.setValue(HugeMushroomBlock.EAST, x > innerRadius)
									.setValue(HugeMushroomBlock.NORTH, z < -innerRadius)
									.setValue(HugeMushroomBlock.SOUTH, z > innerRadius);
							}
							setBlock(levelAccessor, mutableBlockPos, blockState);
						}
					}
				}
			}
		}
	}
}
