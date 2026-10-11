package twilightforest.init;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import twilightforest.TFCommon;

public class TFPOITypes {
	public static final ResourceKey<PoiType> GHAST_TRAP_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, TFCommon.prefix("ghast_trap"));
	public static final PoiType GHAST_TRAP = PoiTypes.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, GHAST_TRAP_KEY, ImmutableSet.copyOf(TFBlocks.GHAST_TRAP.getStateDefinition().getPossibleStates()), 0, 1);
	public static final ResourceKey<PoiType> TWILIGHT_PORTAL_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, TFCommon.prefix("twilight_portal"));
	public static final PoiType TWILIGHT_PORTAL = PoiTypes.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, TWILIGHT_PORTAL_KEY, ImmutableSet.copyOf(TFBlocks.TWILIGHT_PORTAL.getStateDefinition().getPossibleStates()), 0, 1);

	public static void init() {
		TFCommon.LOGGER.info("Initializing poi types...");
		// Existing worlds may have valid POI sections saved before portals were indexed.
		ServerChunkEvents.CHUNK_LOAD.register((level, chunk, newlyGenerated) -> {
			var poiManager = level.getPoiManager();
			var portalType = level.registryAccess().lookupOrThrow(Registries.POINT_OF_INTEREST_TYPE).getOrThrow(TWILIGHT_PORTAL_KEY);
			for (int index = 0; index < chunk.getSections().length; index++) {
				var section = chunk.getSection(index);
				if (!section.maybeHas(state -> state.is(TFBlocks.TWILIGHT_PORTAL)))
					continue;
				SectionPos.of(chunk.getPos(), chunk.getSectionYFromSectionIndex(index)).blocksInside().forEach(pos -> {
					if (chunk.getBlockState(pos).is(TFBlocks.TWILIGHT_PORTAL) && !poiManager.existsAtPosition(TWILIGHT_PORTAL_KEY, pos)) {
						poiManager.add(pos.immutable(), portalType);
					}
				});
			}
		});
	}
}
