package rocks.minestom.listener;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

public final class ChunkUnloadListener implements EventListener<PlayerChunkUnloadEvent> {
    @Override
    public @NotNull Class<PlayerChunkUnloadEvent> eventType() {
        return PlayerChunkUnloadEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerChunkUnloadEvent event) {
        var chunk = event.getInstance().getChunkAt(event.getChunkX(), event.getChunkZ());

        if (chunk != null && chunk.getViewers().isEmpty()) {
            event.getInstance().unloadChunk(chunk);
        }

        return Result.SUCCESS;
    }
}
