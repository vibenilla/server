package rocks.minestom.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import rocks.minestom.Server;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public final class ConnectListener implements EventListener<AsyncPlayerConfigurationEvent> {
    private static final int SPAWN_X = 0;
    private static final int SPAWN_Z = 0;
    private static final int PRELOAD_RADIUS = 2;

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        var player = event.getPlayer();
        var instance = Server.overworld;

        event.setSpawningInstance(instance);
        loadChunks(instance, SPAWN_X, SPAWN_Z, PRELOAD_RADIUS).join();
        var respawnPoint = new Pos(SPAWN_X + 0.5D, this.findHighestBlock(instance, SPAWN_X, SPAWN_Z) + 1.0D, SPAWN_Z + 0.5D);
        player.setRespawnPoint(respawnPoint);

        instance.sendMessage(Component.text(
                event.getPlayer().getUsername() + " joined the game",
                NamedTextColor.YELLOW));

        return Result.SUCCESS;
    }

    private int findHighestBlock(Instance instance, int x, int z) {
        var dimensionType = MinecraftServer.getDimensionTypeRegistry().get(instance.getDimensionType());
        assert dimensionType != null;

        for (int y = dimensionType.maxY() - 1; y >= dimensionType.minY(); y--) {
            var block = instance.getBlock(x, y, z);

            if (!block.isAir() && block.isSolid()) {
                return y;
            }
        }

        return 64;
    }

    private static CompletableFuture<Void> loadChunks(Instance instance, int centerBlockX, int centerBlockZ, int radius) {
        var futures = new ArrayList<CompletableFuture<?>>();
        var centerChunkX = centerBlockX >> 4;
        var centerChunkZ = centerBlockZ >> 4;

        for (var chunkX = centerChunkX - radius; chunkX <= centerChunkX + radius; chunkX++) {
            for (var chunkZ = centerChunkZ - radius; chunkZ <= centerChunkZ + radius; chunkZ++) {
                futures.add(instance.loadChunk(chunkX, chunkZ));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}
