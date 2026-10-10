package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import twilightforest.TFCommon;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;

public class TFStructurePlacementTypes {

	public static final MapCodec<LandmarkGridPlacement> GRID_LANDMARK_PLACEMENT_TYPE = registerPlacer("landmark_grid", LandmarkGridPlacement.CODEC);
	public static final MapCodec<AvoidLandmarkGridPlacement> AVOID_GRID_LANDMARK_PLACEMENT_TYPE = registerPlacer("avoid_landmark_grid", AvoidLandmarkGridPlacement.CODEC);

	private static <P extends StructurePlacement> MapCodec<P> registerPlacer(String name, MapCodec<P> factory) {
		return Registry.register(
			BuiltInRegistries.STRUCTURE_PLACEMENT,
			TFCommon.prefix(name),
			factory
		);
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing structure placement types...");
	}
}