package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.init.TFBlocks;
import twilightforest.util.landmarks.LandmarkUtil;

import java.util.Optional;

public class UndergroundPlantFeature implements Feature {
	public enum Variant {
		DEFAULT(Integer.MAX_VALUE, false), IN_STRUCTURE(Integer.MAX_VALUE, true),
		OREBERRIES(1, false), TROLL_VINES(Integer.MAX_VALUE, true);
		final int maxCount;
		final boolean spawnInStructure;
		Variant(int maxCount, boolean spawnInStructure) {
			this.maxCount = maxCount;
			this.spawnInStructure = spawnInStructure;
		}
	}

	public static final MapCodec<UndergroundPlantFeature> CODEC = makeCodec(Variant.DEFAULT);
	public static final MapCodec<UndergroundPlantFeature> IN_STRUCTURE_CODEC = makeCodec(Variant.IN_STRUCTURE);
	public static final MapCodec<UndergroundPlantFeature> OREBERRIES_CODEC = makeCodec(Variant.OREBERRIES);
	public static final MapCodec<UndergroundPlantFeature> TROLL_VINES_CODEC = makeCodec(Variant.TROLL_VINES);
	private final BlockState state;
	private final Variant variant;

	private static MapCodec<UndergroundPlantFeature> makeCodec(Variant variant) {
		return BlockState.CODEC.fieldOf("state").xmap(state -> new UndergroundPlantFeature(state, variant), feature -> feature.state);
	}

	public UndergroundPlantFeature(BlockState state) { this(state, Variant.DEFAULT); }

	public UndergroundPlantFeature(BlockState state, Variant variant) {
		this.state = state;
		this.variant = variant;
	}

	@Override
	public MapCodec<UndergroundPlantFeature> codec() {
		return switch (this.variant) {
			case DEFAULT -> CODEC;
			case IN_STRUCTURE -> IN_STRUCTURE_CODEC;
			case OREBERRIES -> OREBERRIES_CODEC;
			case TROLL_VINES -> TROLL_VINES_CODEC;
		};
	}
	@Override
	public boolean place(WorldGenLevel featureLevel, ChunkGenerator generator, RandomSource featureRandom, BlockPos featureOrigin) {
		WorldGenLevel world = featureLevel;
		BlockPos origin = featureOrigin;
		RandomSource random = featureRandom;

		int x = origin.getX();
		int z = origin.getZ();
		int placed = 0;

		for (int y = origin.getY(); y > world.getMinY(); y--) {
			if (placed >= this.variant.maxCount)
				break;

			BlockPos pos = new BlockPos(x, y, z);
			if (!world.isEmptyBlock(pos) || random.nextInt(6) == 0) {
				x = origin.getX() + random.nextInt(4) - random.nextInt(4);
				z = origin.getZ() + random.nextInt(4) - random.nextInt(4);
				continue;
			}

			BlockState state = this.state;
			Optional<StructureStart> structureStart = LandmarkUtil.locateNearestLandmarkStart(world, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
			if (state.is(TFBlocks.TROLLVIDR) && random.nextInt(10) == 0)
				state = TFBlocks.UNRIPE_TROLLBER.defaultBlockState();
			if (state.canSurvive(world, pos) && (this.variant.spawnInStructure || structureStart.isEmpty() || !structureStart.get().getBoundingBox().isInside(pos))) {
				world.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				placed++;
			}
		}
		return placed > 0;
	}
}
