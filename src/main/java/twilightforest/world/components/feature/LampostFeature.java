package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFBlocks;

public class LampostFeature implements Feature {

	private static final Rotation[] ROTATIONS = Rotation.values();
	//private final BlockState lamp;

	public static final MapCodec<LampostFeature> CODEC = BlockState.CODEC.fieldOf("state").xmap(LampostFeature::new, feature -> feature.state);
	private final BlockState state;

	public LampostFeature(BlockState state) { this.state = state; }

	@Override
	public MapCodec<LampostFeature> codec() { return CODEC; }

	@Override
	public boolean place(WorldGenLevel featureLevel, ChunkGenerator generator, RandomSource featureRandom, BlockPos featureOrigin) {
		WorldGenLevel world = featureLevel;
		BlockPos pos = featureOrigin;
		RandomSource rand = featureRandom;

		// we should start on a grass block
		if (world.getBlockState(pos.below()).getBlock() != Blocks.GRASS_BLOCK) {
			return false;
		}

		// generate a height
		int height = 1 + rand.nextInt(4);

		// is it air or replaceable above our grass block
		for (int dy = 0; dy <= height; dy++) {
			BlockState state = world.getBlockState(pos.above(dy));
			if (!state.isAir() && !state.canBeReplaced()) {
				return false;
			}
		}

		// generate lamp
		for (int dy = 0; dy < height; dy++) {
			world.setBlock(pos.above(dy), TFBlocks.CANOPY_FENCE.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		}
		world.setBlock(pos.above(height), this.state.rotate(ROTATIONS[rand.nextInt(ROTATIONS.length)]), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		return true;
	}
}
