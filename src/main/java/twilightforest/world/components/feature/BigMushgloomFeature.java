package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import twilightforest.util.features.FeatureLogic;
import twilightforest.world.components.feature.TFMushroomFeature;

public class BigMushgloomFeature extends TFMushroomFeature {
	public static final MapCodec<BigMushgloomFeature> CODEC = settingsCodec(BigMushgloomFeature::new);

	@Override
	public MapCodec<? extends BigMushgloomFeature> codec() { return CODEC; }


	public BigMushgloomFeature(AbstractHugeMushroomFeature config) {
		super(config);
	}

	@Override
	public int getTreeHeight(RandomSource rand) {
		return 2 + rand.nextInt(2);
	}

	@Override
	public int getTreeRadiusForHeight(int i, int i1, int foliageRadius, int treeHeight) {
		return treeHeight <= 2 ? 0 : foliageRadius;
	}

	@Override
	protected void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, AbstractHugeMushroomFeature featureConfiguration) {
		int foliageRadius = featureConfiguration.foliageRadius();
		int capHeight = random.nextBoolean() ? 1 : 2;

		for (int y = 0; y < capHeight; y++) {
			for (int x = -foliageRadius; x <= foliageRadius; ++x) {
				for (int z = -foliageRadius; z <= foliageRadius; ++z) {
					mutableBlockPos.setWithOffset(pos, x, height + y, z);
					if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender()) {
						BlockState blockstate = featureConfiguration.capProvider().value().getState(levelAccessor, random, pos);
						blockstate = FeatureLogic.getSphericalMushroomBlockState(blockstate, x, y, z, foliageRadius, capHeight);
						this.setBlock(levelAccessor, mutableBlockPos, blockstate);
					}
				}
			}
		}
	}
}
