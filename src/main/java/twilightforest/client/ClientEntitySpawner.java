package twilightforest.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

// Assigns IDs to local visual effects that are not spawned by server packets
public final class ClientEntitySpawner {

	private static int nextId = -1;

	private ClientEntitySpawner() {
	}

	public static void addLocalEntity(ClientLevel level, Entity entity) {
		int id;
		do {
			id = nextId;
			nextId = nextId == Integer.MIN_VALUE ? -1 : nextId - 1;
		} while (level.getEntity(id) != null);

		// Keep local effects separate from ordinary server IDs
		entity.setId(id);
		level.addEntity(entity);
	}
}
