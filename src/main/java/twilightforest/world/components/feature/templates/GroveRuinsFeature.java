package twilightforest.world.components.feature.templates;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.world.components.processors.StoneBricksVariants;

public class GroveRuinsFeature extends TemplateFeature<Void> {
	private static final Identifier PILLAR = TFCommon.prefix("feature/ruins/grove_pillar");
	private static final Identifier ARCH = TFCommon.prefix("feature/ruins/grove_arch");

	public GroveRuinsFeature() { super(null); }

	public static final MapCodec<GroveRuinsFeature> CODEC = MapCodec.unit(GroveRuinsFeature::new);

	@Override
	public MapCodec<GroveRuinsFeature> codec() { return CODEC; }

	@Nullable
	@Override
	protected StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random) {
		return templateManager.getOrCreate(random.nextBoolean() ? PILLAR : ARCH);
	}

	@Override
	protected void modifySettings(StructurePlaceSettings settings, RandomSource random, Void config) {
		settings.addProcessor(StoneBricksVariants.INSTANCE);
	}
}
