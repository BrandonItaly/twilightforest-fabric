package twilightforest.asm.mixin.coremod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TFCommon;
import twilightforest.asm.hooks.coremod.WorldgenHooks;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.TFCavesCarver;

import java.util.function.Function;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {
	@Inject(
		method = "lambda$buildTerrain$0",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseBasedChunkGenerator;buildSurface(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/biome/BiomeManager;Ljava/util/Set;Lnet/minecraft/world/level/levelgen/material/rule/MaterialRule;)V",
			shift = At.Shift.AFTER
		)
	)
	private void twilightforest$chunkBlanketing(CallbackInfoReturnable<ChunkAccess> cir,
		@Local(argsOnly = true) ChunkAccess chunk,
		@Local(argsOnly = true) WorldGenRegion region) {
		// Preserve the original order: surface, blanketing, then cave carving
		if (region != null) {
			WorldgenHooks.chunkBlanketing(chunk, region);
		}
	}

	@WrapOperation(method = "lambda$applyCarvingMask$0", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/level/levelgen/Aquifer;computeSubstance(IIID)Lnet/minecraft/world/level/block/state/BlockState;"))
	private static BlockState twilightforest$carveTwilightBlock(Aquifer aquifer, int x, int y, int z, double density,
		Operation<BlockState> original,
		@Local(argsOnly = true) ChunkAccess chunk,
		@Local(argsOnly = true) RandomState randomState,
		@Local(argsOnly = true) Function<BlockPos, Holder<Biome>> biomeGetter) {
		BlockPos pos = new BlockPos(x, y, z);
		if (twilightforest$isTwilightBiome(biomeGetter.apply(pos))) {
			return TFCavesCarver.canCarveBlock(chunk, pos) ? TFCavesCarver.getCarveState(chunk, pos, randomState) : null;
		}
		return original.call(aquifer, x, y, z, density);
	}

	@WrapOperation(method = "lambda$applyCarvingMask$0", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/level/chunk/ChunkAccess;setBlockState(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"))
	private static BlockState twilightforest$decorateTwilightCaves(ChunkAccess chunk, BlockPos pos, BlockState state,
		Operation<BlockState> original,
		@Local(argsOnly = true) RandomState randomState,
		@Local(argsOnly = true) Function<BlockPos, Holder<Biome>> biomeGetter) {
		BlockState previous = original.call(chunk, pos, state);
		Holder<Biome> biome = biomeGetter.apply(pos);
		if (previous != null && twilightforest$isTwilightBiome(biome)) {
			TFCavesCarver.decorateCarvedBlock(chunk, pos, state, randomState,
				biome.is(TFBiomes.HIGHLANDS) || biome.is(TFBiomes.HIGHLANDS_UNDERGROUND));
		}
		return previous;
	}

	@Unique
	private static boolean twilightforest$isTwilightBiome(Holder<Biome> biome) {
		return biome.unwrapKey().map(key -> key.identifier().getNamespace().equals(TFCommon.ID)).orElse(false);
	}

	@ModifyArg(
		method = "createNoiseChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseChunk;<init>(Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/Beardifier;Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;Lnet/minecraft/world/level/levelgen/Aquifer$FluidPicker;Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/densityfunction/DensityVolume;)V"
		),
		index = 1
	)
	private Beardifier twilightforest$gatherCustomTerrain(
		Beardifier beardifierOrMarker,
		@Local(argsOnly = true, name = "structureManager") StructureManager structureManager,
		@Local(argsOnly = true) ChunkAccess chunk,
		@Local(argsOnly = true) RandomState randomState
	) {
		return WorldgenHooks.gatherCustomTerrain(beardifierOrMarker, structureManager, chunk, randomState);
	}
}
