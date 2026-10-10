package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.function.Function;

/** Native mushroom settings with Twilight Forest's cap and branch algorithms. */
public abstract class TFMushroomFeature implements AbstractHugeMushroomFeature {
	protected final AbstractHugeMushroomFeature settings;

	protected TFMushroomFeature(AbstractHugeMushroomFeature settings) { this.settings = settings; }

	public static HugeBrownMushroomFeature settings(BlockStateProvider cap, BlockStateProvider stem, int radius, BlockPredicate ground) {
		return settings(Holder.direct(cap), Holder.direct(stem), radius, ground);
	}

	public static HugeBrownMushroomFeature settings(Holder<BlockStateProvider> cap, Holder<BlockStateProvider> stem, int radius, BlockPredicate ground) {
		return new HugeBrownMushroomFeature(cap, stem, radius, ground);
	}

	protected static <F extends TFMushroomFeature> MapCodec<F> settingsCodec(Function<AbstractHugeMushroomFeature, F> factory) {
		return HugeBrownMushroomFeature.CODEC.xmap(factory,
			feature -> settings(feature.capProvider(), feature.stemProvider(), feature.foliageRadius(), feature.canPlaceOn()));
	}

	@Override public Holder<BlockStateProvider> capProvider() { return this.settings.capProvider(); }
	@Override public Holder<BlockStateProvider> stemProvider() { return this.settings.stemProvider(); }
	@Override public int foliageRadius() { return this.settings.foliageRadius(); }
	@Override public BlockPredicate canPlaceOn() { return this.settings.canPlaceOn(); }

	@Override
	public int getTreeRadiusForHeight(int trunkHeight, int treeHeight, int radius, int y) {
		return this.settings.getTreeRadiusForHeight(trunkHeight, treeHeight, radius, y);
	}

	protected boolean isReplaceable(BlockGetter level, BlockPos pos) {
		return !level.getBlockState(pos).is(BlockTags.FEATURES_CANNOT_REPLACE);
	}

	@Override
	public void makeCap(WorldGenLevel level, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos cursor) {
		this.makeCap(level, random, pos, height, cursor, this.settings);
	}

	protected abstract void makeCap(WorldGenLevel level, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos cursor, AbstractHugeMushroomFeature settings);

}
