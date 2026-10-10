package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.config.TFConfig;
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

	public static void handle(SyncUncraftingTableConfigPacket message, ClientPlayNetworking.Context ctx) {
		Runnable task = () -> {
			TFConfig.uncraftingXpCostMultiplier = message.uncraftingMultiplier();
			TFConfig.repairingXpCostMultiplier = message.repairingMultiplier();
			TFConfig.allowShapelessUncrafting = message.allowShapeless();
			TFConfig.disableIngredientSwitching = message.disableIngredientSwitching();
			TFConfig.disableUncraftingOnly = message.disabledUncrafting();
			TFConfig.disableEntireTable = message.disabledTable();
			TFConfig.disableUncraftingRecipes = message.disabledRecipes();
			TFConfig.reverseRecipeBlacklist = message.flipRecipeList();
			TFConfig.blacklistedUncraftingModIds = message.disabledModids();
			TFConfig.flipUncraftingModIdList = message.flipModidList();
		};

		if (ctx.client().isSameThread()) {
			task.run();
		} else {
			ctx.client().schedule(task);
		}
	}
}