package rocks.minestom.listener;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

public final class ItemDropListener implements EventListener<ItemDropEvent> {
    @Override
    public @NotNull Class<ItemDropEvent> eventType() {
        return ItemDropEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull ItemDropEvent event) {
        var playerPosition = event.getPlayer().getPosition();
        var entity = new ItemEntity(event.getItemStack());
        entity.setPickupDelay(40, TimeUnit.SERVER_TICK);
        entity.setInstance(event.getInstance(), playerPosition.add(0.0D, event.getPlayer().getEyeHeight() - 0.3F, 0.0D));
        entity.setVelocity(playerPosition.direction().mul(6.0D));
        entity.scheduleRemove(5, TimeUnit.MINUTE);
        return Result.SUCCESS;
    }
}
