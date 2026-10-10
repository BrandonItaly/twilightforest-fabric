package twilightforest.datagen.data.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import twilightforest.init.TFCaveCarvers;
import twilightforest.world.components.TFCavesCarver;

public final class TFCaveCarverGenerator {
    public static void bootstrap(BootstrapContext<WorldCarver> context) {
        context.register(TFCaveCarvers.TFCAVES_CONFIGURED, new TFCavesCarver(new CaveWorldCarver(.1F,
            UniformHeight.of(VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(-8)), UniformInt.of(0, 3),
            UniformFloat.of(0, 3), true, ConstantFloat.of(.6F), ConstantFloat.of(1.05F), ConstantFloat.of(1.05F),
            ConstantFloat.of(1), ConstantFloat.of(-.7F)), false));
        context.register(TFCaveCarvers.HIGHLANDCAVES_CONFIGURED, new TFCavesCarver(new CaveWorldCarver(1,
            BiasedToBottomHeight.of(VerticalAnchor.absolute(8), VerticalAnchor.absolute(32), 16), UniformInt.of(0, 1),
            UniformFloat.of(0, 3), true, ConstantFloat.of(.6F), UniformFloat.of(1.1F, 1.3F), ConstantFloat.of(1.1F),
            ConstantFloat.of(1), UniformFloat.of(-.9F, -.65F)), true));
    }
}