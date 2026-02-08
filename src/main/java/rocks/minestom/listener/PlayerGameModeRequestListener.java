package rocks.minestom.listener;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerGameModeRequestEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerGameModeRequestListener implements EventListener<PlayerGameModeRequestEvent> {
    @Override
    public @NotNull Class<PlayerGameModeRequestEvent> eventType() {
        return PlayerGameModeRequestEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerGameModeRequestEvent event) {
        event.getPlayer().setGameMode(event.getRequestedGameMode());
        return Result.SUCCESS;
    }
}
