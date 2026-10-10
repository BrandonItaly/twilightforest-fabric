package twilightforest.world.components.feature.trees;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import twilightforest.world.components.feature.TFMushroomFeature;

public class BrownCanopyMushroomFeature extends CanopyMushroomFeature {
	public static final MapCodec<BrownCanopyMushroomFeature> CODEC = settingsCodec(BrownCanopyMushroomFeature::new);

	@Override
	public MapCodec<BrownCanopyMushroomFeature> codec() { return CODEC; }

	public BrownCanopyMushroomFeature(AbstractHugeMushroomFeature featureConfigurationCodec) {
		super(featureConfigurationCodec);
	}

	@Override
	protected int getBranches(RandomSource random) {
		return Math.max(random.nextInt(5), 3);
	}

	@Override
	protected double getLength(RandomSource random) {
		return 9 - random.nextInt(2);
	}
}
