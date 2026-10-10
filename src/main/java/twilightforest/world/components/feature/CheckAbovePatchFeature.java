package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.DiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;

//[VanillaCopy] of BaseDiskFeature, but we add a check to make sure the block above is air
public class CheckAbovePatchFeature implements Feature {

	public static final MapCodec<CheckAbovePatchFeature> CODEC = DiskFeature.CODEC.xmap(CheckAbovePatchFeature::new, feature -> feature.config);
	private final DiskFeature config;

	public CheckAbovePatchFeature(DiskFeature config) { this.config = config; }

	@Override
	public MapCodec<CheckAbovePatchFeature> codec() { return CODEC; }

	@Override
	public boolean place(WorldGenLevel featureLevel, ChunkGenerator generator, RandomSource featureRandom, BlockPos featureOrigin) {
		DiskFeature diskconfiguration = this.config;
		BlockPos blockpos = featureOrigin;
		WorldGenLevel worldgenlevel = featureLevel;
		RandomSource randomsource = featureRandom;
		boolean flag = false;
		int i = blockpos.getY();
		int j = i + diskconfiguration.halfHeight();
		int k = i - diskconfiguration.halfHeight() - 1;
		int l = diskconfiguration.radius().sample(randomsource);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-l, 0, -l), blockpos.offset(l, 0, l))) {
			int i1 = blockpos1.getX() - blockpos.getX();
			int j1 = blockpos1.getZ() - blockpos.getZ();
			if (i1 * i1 + j1 * j1 <= l * l) {
				flag |= this.placeColumn(diskconfiguration, worldgenlevel, randomsource, j, k, blockpos$mutableblockpos.set(blockpos1));
			}
		}

		return flag;
	}

	protected boolean placeColumn(DiskFeature config, WorldGenLevel level, RandomSource random, int start, int end, BlockPos.MutableBlockPos mutablePos) {
		boolean flag = false;

		for (int i = start; i > end; --i) {
			mutablePos.setY(i);
			if (config.target().test(level, mutablePos) && level.getBlockState(mutablePos.above()).canBeReplaced()) {
				BlockState blockstate1 = config.stateProvider().value().getState(level, random, mutablePos);
				level.setBlock(mutablePos, blockstate1, Block.UPDATE_CLIENTS);
				this.markAboveForPostProcessing(level, mutablePos);
				flag = true;
			}
		}

		return flag;
	}
}
