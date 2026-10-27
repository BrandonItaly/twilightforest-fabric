package twilightforest.util.multiparts;

import carminite.entity.IMultiPartEntity;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import twilightforest.network.UpdateTFMultipartPacket;

public class MultipartEntityUtil {
	public static final MultipartEntityUtil INSTANCE = new MultipartEntityUtil();

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity instanceof IMultiPartEntity multiPartEntity && multiPartEntity.isMultipartEntity()) {
			var players = PlayerLookup.tracking(entity);
			if (!players.isEmpty()) {
				var packet = new UpdateTFMultipartPacket(entity);
				for (ServerPlayer player : players) {
					ServerPlayNetworking.send(player, packet);
				}
			}
		}
		return entity;
	}

}
