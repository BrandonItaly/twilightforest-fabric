package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.codec.RegistryFileCodec;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.*;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFRegistries;
import twilightforest.world.components.layer.BiomeDensitySource;

/**
 * A DensityFunction implementation that enables Biomes to influence terrain formulations, if in the noise chunk generator.
 */
public class NoiseDensityRouter implements DensityFunction {
	public static final MapCodec<NoiseDensityRouter> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).fieldOf("terrain_source").forGetter(NoiseDensityRouter::biomeDensitySourceHolder),
		Codec.doubleRange(-64, 0).fieldOf("lower_density_bound").forGetter(NoiseDensityRouter::lowerDensityBound),
		Codec.doubleRange(0, 64).fieldOf("upper_density_bound").forGetter(NoiseDensityRouter::upperDensityBound),
		Codec.doubleRange(0, 32).orElse(8.0).fieldOf("depth_scalar").forGetter(NoiseDensityRouter::depthScalar)
	).apply(inst, NoiseDensityRouter::new));

	private final Holder<BiomeDensitySource> biomeDensitySourceHolder;
	private final double lowerDensityBound;
	private final double upperDensityBound;
	private final double depthScalar;

	/**
	 * @param biomeDensitySource A BiomeDensitySource containing TerrainColumns, providing per-biome scaling and depth behavior that allows biomes to distinguish their landscapes.
	 * @param lowerDensityBound  Lower clamp bound
	 * @param upperDensityBound  Upper clamp bound
	 */
	public NoiseDensityRouter(Holder<BiomeDensitySource> biomeDensitySource, double lowerDensityBound, double upperDensityBound, double depthScalar) {
		this.biomeDensitySourceHolder = biomeDensitySource;
		this.lowerDensityBound = lowerDensityBound;
		this.upperDensityBound = upperDensityBound;
		this.depthScalar = depthScalar;
	}

	@Override
	public DensitySampler compileSampler(CompileContext compileContext) {
		BiomeDensitySource.Compiled terrain = biomeDensitySourceHolder.value().compile(compileContext);

		return new DensitySampler() {
			@Override
			public float sampleValue(SamplerContext context, int x, int y, int z) {
				BiomeDensitySource.DensityData data = terrain.sampleTerrain(context, x, y, z);
				return (float) data.scale;
			}
			@Override
			public void sampleVolume(SamplerContext context, DensityBuffer output, DensityVolume volume) {
				DensitySampler.sampleVolumeNaive(context, output, volume, this);
			}
		};
	}

	@Override
	public Interval range() { return Interval.encapsulating((float) lowerDensityBound, (float) upperDensityBound); }
	@Override
	public int domainAxes() { return this.biomeDensitySourceHolder.value().domainAxes(); }
	@Override
	public MapCodec<? extends DensityFunction> codec() { return CODEC; }
	public Holder<BiomeDensitySource> biomeDensitySourceHolder() {
		return this.biomeDensitySourceHolder;
	}

	public double lowerDensityBound() {
		return this.lowerDensityBound;
	}

	public double upperDensityBound() {
		return this.upperDensityBound;
	}

	public double depthScalar() {
		return this.depthScalar;
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) { return this; }
}
