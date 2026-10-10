package twilightforest.world.components;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarverOutput;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.List;

//Framework taken from CaveWorldCarver, everything worth knowing is documented for easier changes in the future
public class TFCavesCarver implements WorldCarver {
	public static final MapCodec<TFCavesCarver> CODEC = CaveWorldCarver.MAP_CODEC.xmap(c -> new TFCavesCarver(c, false), c -> c.settings);
	public static final MapCodec<TFCavesCarver> HIGHLAND_CODEC = CaveWorldCarver.MAP_CODEC.xmap(c -> new TFCavesCarver(c, true), c -> c.settings);
	private static final Identifier ORE_RANDOM = Identifier.withDefaultNamespace("ore");
	// Since this object is constructed on game bootup instead of world creation, we can't use world seed
	// Making a different random one each bootup will only screw up determinism, and across separate sessions chunkgen will produce discontinuous noise edges
	// We only need the noise for one thing anyway, and that's placing dirt
	private static final PerlinNoise DIRT_NOISE = new PerlinNoise(new LegacyRandomSource(6972119253061020355L));
	private static final CarverWallProvider DIRT = new NoiseCarverWallProvider(6972119253061020355L,
		NormalNoise.createParity(0, 1), .5F, List.of(Blocks.DIRT.defaultBlockState(), Blocks.DIRT.defaultBlockState(),
		Blocks.ROOTED_DIRT.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.DIRT.defaultBlockState(),
		Blocks.COARSE_DIRT.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.DIRT.defaultBlockState()));
	private static final WeightedList<BlockState> HIGHLAND_WALLS = WeightedList.<BlockState>builder()
		.add(TFBlocks.TROLLSTEINN.defaultBlockState(), 1).add(Blocks.STONE.defaultBlockState(), 3).build();

	private final CaveWorldCarver settings;
	private final boolean isHighlands;

	public TFCavesCarver(CaveWorldCarver settings, boolean isHighlands) {
		this.settings = settings;
		this.isHighlands = isHighlands;
	}

	@Override
	public MapCodec<TFCavesCarver> codec() {
		return this.isHighlands ? HIGHLAND_CODEC : CODEC;
	}

	@Override
	public boolean isStartChunk(RandomSource random) {
		// Highland caves instead spawn when the troll caves structure is nearby, and with special location ruls
		return this.isHighlands || random.nextFloat() <= this.settings.probability();
	}

	@Override
	public boolean carve(WorldGenerationContext ctx, RandomSource random, ChunkPos chunkPos, ChunkPos accessPos, CarverOutput output) {
		if (this.isHighlands && (Mth.clamp(LegacyLandmarkPlacements.manhattanDistanceFromLandmarkCenter(accessPos.x(), accessPos.z()), 0, 0b11) & 0b1) == 1)
			return false; // If highlands, enforces a binary grid (diagonal range of 4 chunks) of possible placements around the structure center, with center being one of the zero tiles

		int i = SectionPos.sectionToBlockCoord(this.getRange() * 2 - 1);

		// If highlands, only roll chance to generate even 1 cave. Otherwise, limited caves spawn for regular TF underground
		int caveCount = this.isHighlands ? random.nextInt(2) : random.nextInt(this.getCaveBound());

		for (int caveIndex = 0; caveIndex < caveCount; ++caveIndex) {
			double x = accessPos.getBlockX(random.nextInt(16));
			double y = this.settings.y().sample(random, ctx);
			double z = accessPos.getBlockZ(random.nextInt(16));
			double horiz = this.settings.horizontalRadiusMultiplier().sample(random);
			double vert = this.settings.verticalRadiusMultiplier().sample(random);
			double floor = this.settings.floorLevel().sample(random);
			CarveSkipChecker checker = (dX, dY, dZ, yPos) -> shouldSkip(dX, dY, dZ, floor);
			int tunnelCount = 1;
			if (this.isHighlands || random.nextInt(4) == 0) {
				double horizToVertRatio = this.settings.roomVerticalRadiusMultiplier().sample(random);
				float radius = 1.0F + random.nextFloat() * 6.0F;
				this.createRoom(chunkPos, output, x, y, z, radius, horizToVertRatio, checker);
				tunnelCount += random.nextInt(4);
			}

			for (int tunnelIndex = 0; tunnelIndex < tunnelCount; ++tunnelIndex) {
				float randomRadian = random.nextFloat() * Mth.TWO_PI;
				float randomPitch = (random.nextFloat() - 0.5F) / 4.0F;
				float thickness = this.getThickness(random);
				int branchCount = i - random.nextInt(i / 4);

				this.createTunnel(chunkPos, output, ctx.getMinGenY(), random.nextLong(), x, y, z, horiz, vert, thickness,
					randomRadian, randomPitch, 0, branchCount, this.getYScale(), checker);
			}
		}

		return true;
	}

	public static boolean canCarveBlock(ChunkAccess access, BlockPos pos) {
		//We dont want caves to go so far down you can see bedrock, so lets stop them right before
		if (pos.getY() < access.getMinY() + 6 || !canReplaceBlock(access.getBlockState(pos))) return false;

		for (Direction facing : Direction.values()) {
			BlockPos relative = pos.relative(facing);
			if (access.getPos().contains(relative) && access.getFluidState(relative).is(FluidTags.WATER)) {
				return false; // If replacing this block will expose any neighboring water, then skip the current position param
			}
		}
		return true;
	}

	public static BlockState getCarveState(ChunkAccess access, BlockPos pos, RandomState randomState) {
		// Sand doesn't quite generate until after the carvers, so we must look for liquid above possible sand instead
		if (!access.getFluidState(pos.above(2)).isEmpty()) {
			RandomSource randomFromPos = randomState.getOrCreateRandomFactory(ORE_RANDOM).at(pos);
			return randomFromPos.nextBoolean() ? Blocks.ROOTED_DIRT.defaultBlockState() : Blocks.COARSE_DIRT.defaultBlockState(); // normal dirt will get replaced with sand, special ones are required
		}
		return Blocks.CAVE_AIR.defaultBlockState();
	}

	public static void decorateCarvedBlock(ChunkAccess access, BlockPos pos, BlockState carvedState, RandomState randomState, boolean isHighlands) {
		RandomSource randomFromPos = randomState.getOrCreateRandomFactory(ORE_RANDOM).at(pos);
		if (!carvedState.is(Blocks.CAVE_AIR)) randomFromPos.nextBoolean();
		postCarveBlock(access, pos, randomFromPos, isHighlands);
	}

	private static void postCarveBlock(ChunkAccess access, BlockPos pos, RandomSource rand, boolean isHighlands) {
		for (Direction facing : Direction.values()) {
			BlockPos directionalRelative = pos.relative(facing);
			if (!access.getPos().contains(directionalRelative)) continue;
			BlockState neighboringBlock = access.getBlockState(directionalRelative);

			// FIXME Half-way configurable, would prefer to eliminate the isHighlands check entirely
			//  The rand.nextInt rolls should have some way of being set into a custom config as well

			if (isHighlands) {
				if (rand.nextInt(4) == 0 && canReplaceBlock(neighboringBlock)) {
					access.setBlockState(directionalRelative, HIGHLAND_WALLS.getRandomOrThrow(rand));
				}
			} else if (facing != Direction.DOWN && (facing == Direction.UP || access.getBlockState(directionalRelative.above()).isAir()
				|| checkNoiseThreshold(directionalRelative, 0.25F, 0.5F))) { //here's the code for making dirt roofs. Enjoy :)
				// Dirt is never placed below, always on roof, and typically to the sides
				if (neighboringBlock.is(BlockTags.BASE_STONE_OVERWORLD) || neighboringBlock.getFluidState().is(FluidTags.WATER)) {
					access.setBlockState(directionalRelative, DIRT.getState(rand, directionalRelative));
				}
			}
		}
	}

	private static boolean checkNoiseThreshold(BlockPos pos, double posScalar, double threshold) {
		double noise = DIRT_NOISE.get(pos.getX() * posScalar, pos.getY() * posScalar, pos.getZ() * posScalar);
		// Noise outputs values between -1 to 1, we must normalize it using n * 0.5 + 0.5
		return noise * 0.5 + 0.5 > threshold;
	}

	protected static boolean canReplaceBlock(BlockState state) {
		return state.is(TFBlockTags.CARVER_REPLACEABLES) && !state.is(BlockTags.ICE) && !state.getFluidState().is(FluidTags.WATER);
	}

	protected int getCaveBound() {
		return 4;
	}

	protected float getThickness(RandomSource rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getYScale() {
		return 1.0D;
	}

	private static boolean shouldSkip(double posX, double posY, double posZ, double minY) {
		if (posY <= minY) {
			return true;
		} else {
			return posX * posX + posY * posY + posZ * posZ >= 1.0D;
		}
	}

	protected void createRoom(ChunkPos chunkPos, CarverOutput output, double posX, double posY, double posZ, float radius, double horizToVertRatio, CarveSkipChecker checker) {
		double d0 = 1.5D + (double) (Mth.sin(((float) Math.PI / 2F)) * radius);
		double d1 = d0 * horizToVertRatio;
		WorldCarver.carveEllipsoid(chunkPos, posX, posY, posZ, d0, d1, output, checker);
	}

	protected void createTunnel(ChunkPos chunkPos, CarverOutput output, int minY, long seed, double posX, double posY, double posZ, double horizMult, double vertMult, float thickness, float yaw, float pitch, int branchIndex, int branchCount, double horizToVertRatio, CarveSkipChecker checker) {
		RandomSource random = RandomSource.create(seed);
		int i = random.nextInt(branchCount / 2) + branchCount / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for (int j = branchIndex; j < branchCount; ++j) {
			double horizontalRadius = 1.5D + (double) (Mth.sin((float) Math.PI * (float) j / (float) branchCount) * thickness);
			double verticalRadius = horizontalRadius * horizToVertRatio;
			float f2 = Mth.cos(pitch);
			posX += Mth.cos(yaw) * f2;

			float yShift = Mth.sin(pitch);
			// If posY nears bedrock, "slow" its descent if marching downwards
			posY += yShift > 0 || posY + yShift > minY + 10 ? yShift : yShift * 0.25f;

			posZ += Mth.sin(yaw) * f2;
			pitch = pitch * (flag ? 0.92F : 0.7F);
			pitch = pitch + f1 * 0.1F;
			yaw += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && thickness > 1.0F) {
				this.createTunnel(chunkPos, output, minY, random.nextLong(), posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw - ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, checker);
				this.createTunnel(chunkPos, output, minY, random.nextLong(), posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw + ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, checker);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!WorldCarver.canReach(chunkPos, posX, posZ, j, branchCount, thickness)) {
					return;
				}

				// Additional size-boosting to make wider & taller spherical rooms
				boolean shouldEnlargeSphere = posY > minY + 12 && random.nextInt(48) == 0;
				float sizeMultiplier = shouldEnlargeSphere
					? random.nextFloat() * random.nextFloat() * 2f + 1
					: 1;

				double sphereHRadius = Math.min(horizontalRadius * horizMult * sizeMultiplier, 10);
				double sphereVRadius = verticalRadius * vertMult * sizeMultiplier;
				// If side-boosting is applied, then squish the sphere's edge-steeped floor into a dish
				double sphereVRadiusLimited = shouldEnlargeSphere ? Math.min(sphereVRadius, sphereHRadius * 0.65f) : sphereVRadius;

				WorldCarver.carveEllipsoid(chunkPos, posX, posY, posZ, sphereHRadius, sphereVRadiusLimited, output, checker);
			}
		}

	}
}
