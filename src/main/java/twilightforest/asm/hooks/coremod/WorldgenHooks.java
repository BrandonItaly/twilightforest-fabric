package twilightforest.asm.hooks.coremod;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.world.components.chunkgenerators.CustomTerrainBeardifier;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

import java.util.List;

public final class WorldgenHooks {
	public static Beardifier gatherCustomTerrain(Beardifier vanilla, StructureManager structureManager, ChunkAccess chunkAccess, RandomState randomState) {
		ChunkPos chunkPos = chunkAccess.getPos();
		List<StructureStart> structureStarts = structureManager.startsForStructure(chunkPos.x(), chunkPos.z(), s -> s instanceof CustomDensitySource);

		if (structureStarts.isEmpty())
			return vanilla;

		ObjectArrayList<DensitySampler> customStructureTerraforms = new ObjectArrayList<>(structureStarts.size());

		for (StructureStart structureStart : structureStarts)
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(randomState.getSampler(customDensitySource.getStructureTerraformer(chunkPos, structureStart)));

		return customStructureTerraforms.isEmpty() ? vanilla : new CustomTerrainBeardifier(vanilla, customStructureTerraforms);
	}

	public static void chunkBlanketing(ChunkAccess access, WorldGenRegion region) {
		ChunkBlanketProcessors.chunkBlanketing(access, region);
	}

	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}
}
