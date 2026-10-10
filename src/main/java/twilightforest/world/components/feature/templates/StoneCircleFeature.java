package twilightforest.world.components.feature.templates;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.world.components.processors.CobbleVariants;

public class StoneCircleFeature extends TemplateFeature<Void> {
	private static final Identifier STONE_CIRCLE = TFCommon.prefix("feature/ruins/stone_circle");

	public StoneCircleFeature() { super(null); }

	public static final MapCodec<StoneCircleFeature> CODEC = MapCodec.unit(StoneCircleFeature::new);

	@Override
	public MapCodec<StoneCircleFeature> codec() { return CODEC; }

	@Nullable
	@Override
	protected StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
		return templateManager.getOrCreate(STONE_CIRCLE);
	}

	@Override
	protected void modifySettings(StructurePlaceSettings settings, RandomSource random, Void config) {
		settings.addProcessor(CobbleVariants.INSTANCE);
	}
}
