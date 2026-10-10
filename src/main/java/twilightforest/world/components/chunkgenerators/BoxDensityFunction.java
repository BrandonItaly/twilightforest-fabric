package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.List;

public class BoxDensityFunction implements CoordinateDensityFunction {
	public static final MapCodec<BoxDensityFunction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.INT.fieldOf("minX").forGetter(f -> f.minX),
		Codec.INT.fieldOf("minY").forGetter(f -> f.minY),
		Codec.INT.fieldOf("minZ").forGetter(f -> f.minZ),
		Codec.INT.fieldOf("maxX").forGetter(f -> f.maxX),
		Codec.INT.fieldOf("maxY").forGetter(f -> f.maxY),
		Codec.INT.fieldOf("maxZ").forGetter(f -> f.maxZ),
		Codec.DOUBLE.fieldOf("minValue").forGetter(f -> f.minValue),
		Codec.DOUBLE.fieldOf("maxValue").forGetter(f -> f.maxValue),
		TerrainAdjustment.CODEC.fieldOf("beardifier").forGetter(f -> f.terrainAdjustment)
	).apply(inst, BoxDensityFunction::new));

	private final int minX, minY, minZ, maxX, maxY, maxZ;
	private final double minValue, maxValue;
	private final TerrainAdjustment terrainAdjustment;
	private final Beardifier sampler;

	public static DensityFunction combine(List<BoundingBox> boxes, int dYMin, int dYMax, TerrainAdjustment terrainAdjustment) {
		if (boxes.isEmpty()) return DensityFunctions.constant(0);

		DensityFunction densityFunction = make(boxes.getFirst(), dYMin, dYMax, terrainAdjustment);

		for (int idx = 1; idx < boxes.size(); idx++) {
			densityFunction = DensityFunctions.add(make(boxes.get(idx), dYMin, dYMax, terrainAdjustment), densityFunction);
		}

		return densityFunction;
	}

	public static BoxDensityFunction make(BoundingBox box, int dYMin, int dYMax, TerrainAdjustment terrainAdjustment) {
		return new BoxDensityFunction(box.minX(), box.minY() + dYMin, box.minZ(), box.maxX(), box.maxY() + dYMax, box.maxZ(), -4.0, 4.0, terrainAdjustment);
	}

	public BoxDensityFunction(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, double minValue, double maxValue, TerrainAdjustment terrainAdjustment) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.terrainAdjustment = terrainAdjustment;
		BoundingBox box = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		this.sampler = new Beardifier(List.of(new Beardifier.Rigid(box, terrainAdjustment, 0)), List.of(), box.inflatedBy(24));
	}

	@Override
	public float compute(int blockX, int blockY, int blockZ) {
		return (float)Mth.clamp(this.sampler.sampleValue(SamplerContext.EMPTY_UNCACHED, blockX, blockY, blockZ), this.minValue, this.maxValue);
	}

	@Override
	public Interval range() {
		return Interval.of((float)this.minValue, (float)this.maxValue);
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}
}
