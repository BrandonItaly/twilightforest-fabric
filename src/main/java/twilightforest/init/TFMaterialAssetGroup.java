package twilightforest.init;

import net.minecraft.resources.Identifier;
import twilightforest.TFCommon;

public final class TFMaterialAssetGroup {
	public static final Identifier IRONWOOD = create("ironwood");
	public static final Identifier STEELEAF = create("steeleaf");
	public static final Identifier KNIGHTMETAL = create("knightmetal");
	public static final Identifier FIERY = create("fiery");
	public static final Identifier NAGA_SCALE = create("naga_scale");
	public static final Identifier CARMINITE = create("carminite");

	public static Identifier create(String palette) { return TFCommon.prefix(palette); }
}