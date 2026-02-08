package rocks.minestom.listener;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public final class PickupItemListener implements EventListener<PickupItemEvent> {
    @Override
    public @NotNull Class<PickupItemEvent> eventType() {
        return PickupItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            // TODO: pick up part of a stack if your inventory is full
            var random = ThreadLocalRandom.current();
            var itemPosition = event.getItemEntity().getPosition();
            player.getInventory().addItemStack(event.getItemStack());
            player.getInstance().playSound(
                    Sound.sound(
                            SoundEvent.ENTITY_ITEM_PICKUP,
                            Sound.Source.PLAYER,
                            0.2F,
                            (random.nextFloat() - random.nextFloat()) * 1.4F + 2.0F),
                    itemPosition.x(),
                    itemPosition.y(),
                    itemPosition.z());
        }

        return Result.SUCCESS;
    }
}
