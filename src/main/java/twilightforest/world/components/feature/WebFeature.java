package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public class WebFeature implements Feature {

	public static final MapCodec<WebFeature> CODEC = MapCodec.unit(WebFeature::new);

	@Override
	public MapCodec<WebFeature> codec() {
		return CODEC;
	}

	private static boolean isValidMaterial(BlockState state) {
		return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
	}

	@Override
	public boolean place(WorldGenLevel featureLevel, ChunkGenerator generator, RandomSource featureRandom, BlockPos featureOrigin) {
		WorldGenLevel world = featureLevel;
		BlockPos pos = featureOrigin.above(featureRandom.nextInt(world.getMaxY() - featureOrigin.getY()));
		while (pos.getY() > featureOrigin.getY()) {
			pos = pos.below();
			BlockState state = world.getBlockState(pos);
			if (world.isEmptyBlock(pos.below()) && isValidMaterial(state)) {
				world.setBlock(state.is(BlockTags.LEAVES) && featureRandom.nextBoolean() ? pos : pos.below(), Blocks.COBWEB.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				return true;
			}
		}

		return false;
	}
}
