package twilightforest.world.components.chunkgenerators;

import net.minecraft.world.level.levelgen.densityfunction.*;

public interface CoordinateDensityFunction extends DensityFunction {
	float compute(int blockX, int blockY, int blockZ);

	@Override
	default DensitySampler compileSampler(CompileContext context) {
		return new DensitySampler() {
			@Override
			public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
				return CoordinateDensityFunction.this.compute(blockX, blockY, blockZ);
			}

			@Override
			public void sampleVolume(SamplerContext context, DensityBuffer output, DensityVolume volume) {
				DensitySampler.sampleVolumeNaive(context, output, volume, this);
			}
		};
	}

	@Override
	default DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	@Override
	default int domainAxes() {
		return ALL_AXES;
	}
}
