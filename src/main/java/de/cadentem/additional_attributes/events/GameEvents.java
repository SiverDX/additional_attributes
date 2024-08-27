package de.cadentem.additional_attributes.events;

import de.cadentem.additional_attributes.registry.AAttributes;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class GameEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void amplyLootingLevel(final LootingLevelEvent event) { // FIXME 1.21 :: event is dead
        event.setLootingLevel(AAttributes.getIntValue(event.getEntity(), AAttributes.LOOTING.get(), event.getLootingLevel()));
    }
}
