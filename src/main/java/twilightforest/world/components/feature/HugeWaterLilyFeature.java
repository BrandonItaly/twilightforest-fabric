package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFBlocks;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class HugeWaterLilyFeature implements Feature {

	public static final MapCodec<HugeWaterLilyFeature> CODEC = MapCodec.unit(HugeWaterLilyFeature::new);

	@Override
	public MapCodec<HugeWaterLilyFeature> codec() {
		return CODEC;
	}

	@Override
	public boolean place(WorldGenLevel featureLevel, ChunkGenerator generator, RandomSource featureRandom, BlockPos featureOrigin) {
		WorldGenLevel world = featureLevel;
		BlockPos pos = featureOrigin;
		RandomSource random = featureRandom;

		for (int i = 0; i < 4; i++) {
			BlockPos pos_ = pos.offset(
				random.nextInt(8) - random.nextInt(8),
				random.nextInt(4) - random.nextInt(4),
				random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, pos_)) {
				world.setBlock(pos_, TFBlocks.HUGE_WATER_LILY.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(LevelAccessor world, BlockPos pos) {
		return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(Blocks.WATER);
	}
}
