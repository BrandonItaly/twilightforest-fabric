package twilightforest.world.components.chunkgenerators;

import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.densityfunction.*;

import java.util.List;

public final class CustomTerrainBeardifier extends Beardifier {
	private final Beardifier vanilla;
	private final List<DensitySampler> customDensities;

	public CustomTerrainBeardifier(Beardifier vanilla, List<DensitySampler> customDensities) {
		super(List.of(), List.of(), null);
		this.vanilla = vanilla;
		this.customDensities = List.copyOf(customDensities);
	}

	@Override
	public float sampleValue(SamplerContext context, int x, int y, int z) {
		float density = this.vanilla.sampleValue(context, x, y, z);

		for (int i = 0; i < this.customDensities.size(); i++) {
			density += this.customDensities.get(i).sampleValue(context, x, y, z);
		}

		return density;
	}

	@Override
	public void sampleVolume(SamplerContext context, DensityBuffer output, DensityVolume volume) {
		this.vanilla.sampleVolume(context, output, volume);
		if (this.customDensities.isEmpty()) return;

		try (ScopedDensityBuffer customOutput = context.acquireBuffer(volume)) {
			for (DensitySampler sampler : this.customDensities) {
				sampler.sampleVolume(context, customOutput, volume);
				for (int i = 0; i < volume.size(); i++) {
					output.addTo(i, customOutput.get(i));
				}
			}
		}
	}
}
