package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import twilightforest.world.components.feature.config.WeightedListFeatureConfig;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * While seemingly similar to RandomFeatureConfiguration, this uses a weighted list and a single random value sample.
 * The goal is to produced pseudorandom result distribution that better match expectations defined by a weighted list.
 */
public class WeightedListFeature implements Feature {
	public static final MapCodec<WeightedListFeature> CODEC = WeightedListFeatureConfig.CODEC.fieldOf("features").xmap(WeightedListFeature::new, feature -> feature.config);
	private final WeightedListFeatureConfig config;

	public WeightedListFeature(WeightedListFeatureConfig config) { this.config = config; }

	@Override
	public MapCodec<WeightedListFeature> codec() { return CODEC; }

	@Override
	public Stream<Holder<Feature>> getSubFeatures() { return this.config.getSubFeatures(); }

	@Override
	public boolean place(WorldGenLevel featureLevel, ChunkGenerator generator, RandomSource featureRandom, BlockPos featureOrigin) {
		WeightedListFeatureConfig weightedListFeatureConfig = this.config;
		RandomSource random = featureRandom;
		WorldGenLevel worldGenLevel = featureLevel;
		ChunkGenerator chunkGenerator = generator;
		BlockPos blockPos = featureOrigin;

		Optional<Holder<PlacedFeature>> randomFeature = weightedListFeatureConfig.getRandomFeature(random);

		//noinspection OptionalIsPresent
		if (randomFeature.isEmpty())
			return false;

		return randomFeature.get().value().place(worldGenLevel, chunkGenerator, random, blockPos);
	}
}
