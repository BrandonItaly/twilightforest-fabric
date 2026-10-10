package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.TFCommon;
import twilightforest.world.components.feature.*;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.components.feature.templates.*;
import twilightforest.world.components.feature.trees.*;
import twilightforest.world.components.feature.trollcave.TrollBigMushgloomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeBrownMushroomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeRedMushroomFeature;

public class TFFeatures {

	public static final MapCodec<TFSmallLakeFeature> SMALL_LAKE = register("small_lake", TFSmallLakeFeature.CODEC);
	public static final MapCodec<BigMushgloomFeature> BIG_MUSHGLOOM = register("big_mushgloom", BigMushgloomFeature.CODEC);
	public static final MapCodec<BrownCanopyMushroomFeature> CANOPY_BROWN_MUSHROOM = register("canopy_brown_mushroom", BrownCanopyMushroomFeature.CODEC);
	public static final MapCodec<RedCanopyMushroomFeature> CANOPY_RED_VANILLA_MUSHROOM = register("canopy_red_vanilla_mushroom", RedCanopyMushroomFeature.VANILLA_CODEC);
	public static final MapCodec<RedCanopyMushroomFeature> CANOPY_RED_SMOOTH_MUSHROOM = register("canopy_red_smooth_mushroom", RedCanopyMushroomFeature.SMOOTH_CODEC);
	public static final MapCodec<RedCanopyMushroomFeature> CANOPY_RED_SPHEROID_MUSHROOM = register("canopy_red_spheroid_mushroom", RedCanopyMushroomFeature.SPHEROID_CODEC);
	public static final MapCodec<RedCanopyMushroomFeature> CANOPY_RED_FLAT_MUSHROOM = register("canopy_red_flat_mushroom", RedCanopyMushroomFeature.FLAT_CODEC);
	public static final MapCodec<MegaOakTreeFeature> MEGA_OAK = register("mega_oak", MegaOakTreeFeature.CODEC);
	public static final MapCodec<MegaCanopyTreeFeature> MEGA_CANOPY = register("mega_canopy", MegaCanopyTreeFeature.CODEC);
	public static final MapCodec<BlockSpikeFeature> CAVE_STALACTITE = register("block_spike", BlockSpikeFeature.CODEC);
	public static final MapCodec<DarkCanopyTreeFeature> DARK_CANOPY_TREE = register("dark_canopy_tree", DarkCanopyTreeFeature.CODEC);
	public static final MapCodec<DruidHutFeature> DRUID_HUT = register("druid_hut", DruidHutFeature.CODEC);
	public static final MapCodec<FallenHollowLogFeature> FALLEN_HOLLOW_LOG = register("fallen_hollow_log", FallenHollowLogFeature.CODEC);
	public static final MapCodec<FallenLeavesFeature> FALLEN_LEAVES = register("fallen_leaves", FallenLeavesFeature.CODEC);
	public static final MapCodec<SmallFallenLogFeature> FALLEN_SMALL_LOG = register("fallen_small_log", SmallFallenLogFeature.CODEC);
	public static final MapCodec<FireJetFeature> FIRE_JET = register("fire_jet", FireJetFeature.CODEC);
	public static final MapCodec<FoundationFeature> FOUNDATION = register("foundation", FoundationFeature.CODEC);
	public static final MapCodec<GraveyardFeature> GRAVEYARD = register("graveyard", GraveyardFeature.CODEC);
	public static final MapCodec<GroveRuinsFeature> GROVE_RUINS = register("grove_ruins", GroveRuinsFeature.CODEC);
	public static final MapCodec<HollowStumpFeature> HOLLOW_STUMP = register("hollow_stump", HollowStumpFeature.CODEC);
	public static final MapCodec<HugeLilypadFeature> HUGE_LILY_PAD = register("huge_lily_pad", HugeLilypadFeature.CODEC);
	public static final MapCodec<HugeWaterLilyFeature> HUGE_WATER_LILY = register("huge_water_lily", HugeWaterLilyFeature.CODEC);
	public static final MapCodec<LampostFeature> LAMPPOSTS = register("lampposts", LampostFeature.CODEC);
	public static final MapCodec<LargeWinterTreeFeature> LARGE_WINTER_TREE = register("large_winter_tree", LargeWinterTreeFeature.CODEC);
	public static final MapCodec<MiningTreeFeature> MINERS_TREE = register("miners_tree", MiningTreeFeature.CODEC);
	public static final MapCodec<MonolithFeature> MONOLITH = register("monolith", MonolithFeature.CODEC);
	public static final MapCodec<CheckAbovePatchFeature> MYCELIUM_BLOB = register("mycelium_blob", CheckAbovePatchFeature.CODEC);
	public static final MapCodec<BerryBushFeature> BERRY_BUSH = register("berry_bush", BerryBushFeature.CODEC);
	public static final MapCodec<UndergroundPlantFeature> UNDERGROUND_PLANTS = register("underground_plants", UndergroundPlantFeature.CODEC);
	public static final MapCodec<UndergroundPlantFeature> UNDERGROUND_PLANTS_IN_STRUCTURE = register("underground_plants_in_structure", UndergroundPlantFeature.IN_STRUCTURE_CODEC);
	public static final MapCodec<UndergroundPlantFeature> OREBERRY_BUSHES = register("oreberry_bushes", UndergroundPlantFeature.OREBERRIES_CODEC);
	public static final MapCodec<UndergroundPlantFeature> TROLL_VINES = register("troll_vines", UndergroundPlantFeature.TROLL_VINES_CODEC);
	public static final MapCodec<TrollBigMushgloomFeature> TROLL_BIG_MUSHGLOOM = register("troll_cave_big_mushgloom", TrollBigMushgloomFeature.CODEC);
	public static final MapCodec<TrollHugeRedMushroomFeature> TROLL_HUGE_RED_MUSHROOM = register("troll_cave_huge_red_mushroom", TrollHugeRedMushroomFeature.CODEC);
	public static final MapCodec<TrollHugeBrownMushroomFeature> TROLL_HUGE_BROWN_MUSHROOM = register("troll_cave_huge_brown_mushroom", TrollHugeBrownMushroomFeature.CODEC);
	public static final MapCodec<StoneCircleFeature> STONE_CIRCLE = register("stone_circle", StoneCircleFeature.CODEC);
	public static final MapCodec<ThornFeature> THORNS = register("thorns", ThornFeature.CODEC);
	public static final MapCodec<TimeTreeFeature> TREE_OF_TIME = register("tree_of_time", TimeTreeFeature.CODEC);
	public static final MapCodec<WebFeature> WEBS = register("webs", WebFeature.CODEC);
	public static final MapCodec<SimpleWellFeature> SIMPLE_WELL = register("simple_well", SimpleWellFeature.CODEC);
	public static final MapCodec<FancyWellFeature> FANCY_WELL = register("fancy_well", FancyWellFeature.CODEC);
	public static final MapCodec<WoodRootFeature> WOOD_ROOTS = register("wood_roots", WoodRootFeature.CODEC);
	public static final MapCodec<SnowUnderTreeFeature> SNOW_UNDER_TREES = register("snow_under_trees", SnowUnderTreeFeature.CODEC);
	public static final MapCodec<SnowTreeFeature> SNOW_TREE = register("anywhere_tree", SnowTreeFeature.CODEC);
	public static final MapCodec<EnchantedForestVinesFeature> ENCHANTED_FOREST_VINES = register("enchanted_forest_vines", EnchantedForestVinesFeature.CODEC);
	public static final MapCodec<WeightedListFeature> WEIGHTED_LIST_SELECTOR = register("weighted_list", WeightedListFeature.CODEC);

	private static <F extends Feature> MapCodec<F> register(String name, MapCodec<F> codec) {
		return Registry.register(BuiltInRegistries.FEATURE_TYPE, TFCommon.prefix(name), codec);
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing features...");
	}
}