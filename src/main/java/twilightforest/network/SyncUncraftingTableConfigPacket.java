package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TFCommon;

import java.util.List;

public record SyncUncraftingTableConfigPacket(
	double uncraftingMultiplier, double repairingMultiplier,
	boolean allowShapeless, boolean disableIngredientSwitching, boolean disabledUncrafting, boolean disabledTable,
	List<? extends String> disabledRecipes, boolean flipRecipeList,
	List<? extends String> disabledModids, boolean flipModidList) implements CustomPacketPayload {

	public static final Type<SyncUncraftingTableConfigPacket> TYPE = new Type<>(TFCommon.prefix("sync_uncrafting_config"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncUncraftingTableConfigPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncUncraftingTableConfigPacket::write, SyncUncraftingTableConfigPacket::new);

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(),
			buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
			net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8.apply(net.minecraft.network.codec.ByteBufCodecs.list()).decode(buf), buf.readBoolean(),
			net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8.apply(net.minecraft.network.codec.ByteBufCodecs.list()).decode(buf), buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.uncraftingMultiplier());
		buf.writeDouble(this.repairingMultiplier());
		buf.writeBoolean(this.allowShapeless());
		buf.writeBoolean(this.disableIngredientSwitching());
		buf.writeBoolean(this.disabledUncrafting());
		buf.writeBoolean(this.disabledTable());
		net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8.apply(net.minecraft.network.codec.ByteBufCodecs.list()).encode(buf, List.copyOf(this.disabledRecipes()));
		buf.writeBoolean(this.flipRecipeList());
		net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8.apply(net.minecraft.network.codec.ByteBufCodecs.list()).encode(buf, List.copyOf(this.disabledModids()));
		buf.writeBoolean(this.flipModidList());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}