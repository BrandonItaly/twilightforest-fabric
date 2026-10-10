package twilightforest.world.components.feature.trees.growers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFConfiguredFeatures;

public final class TFTreeGrowers {
    public static final TreeGrower TWILIGHT_OAK = new TreeGrower("twilight_oak",
        weighted(TFConfiguredFeatures.TWILIGHT_OAK_TREE, TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE),
        weighted(TFConfiguredFeatures.FOREST_MEGA_OAK_TREE, TFConfiguredFeatures.SAVANNAH_MEGA_OAK_TREE),
        WeightedList.of(), TFConfiguredFeatures.TWILIGHT_OAK_TREE);
    public static final TreeGrower CANOPY = new TreeGrower("canopy", WeightedList.of(TFConfiguredFeatures.CANOPY_TREE),
        WeightedList.of(TFConfiguredFeatures.MEGA_CANOPY_TREE), WeightedList.of(), TFConfiguredFeatures.CANOPY_TREE);
    public static final TreeGrower MANGROVE = single("mangrove", TFConfiguredFeatures.MANGROVE_TREE);
    public static final TreeGrower DARK = single("dark", TFConfiguredFeatures.HOMEGROWN_DARKWOOD_TREE);
    public static final TreeGrower TIME = single("time", TFConfiguredFeatures.TIME_TREE);
    public static final TreeGrower TRANSFORMATION = single("transformation", TFConfiguredFeatures.TRANSFORMATION_TREE);
    public static final TreeGrower MINING = single("mining", TFConfiguredFeatures.MINING_TREE);
    public static final TreeGrower SORTING = single("sorting", TFConfiguredFeatures.SORTING_TREE);
    public static final TreeGrower HOLLOW_OAK = new StructureTreeGrower("hollow_oak");
    public static final TreeGrower RAINBOW_OAK = new TreeGrower("rainbow_oak",
        weighted(TFConfiguredFeatures.RAINBOW_OAK_TREE, TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREE),
        WeightedList.of(), WeightedList.of(), TFConfiguredFeatures.RAINBOW_OAK_TREE);

    private static TreeGrower single(String name, ResourceKey<Feature> feature) {
        return new TreeGrower(name, WeightedList.of(feature), WeightedList.of(), WeightedList.of(), feature);
    }

    private static WeightedList<ResourceKey<Feature>> weighted(ResourceKey<Feature> regular, ResourceKey<Feature> rare) {
        return WeightedList.<ResourceKey<Feature>>builder().add(regular, 9).add(rare, 1).build();
    }
}