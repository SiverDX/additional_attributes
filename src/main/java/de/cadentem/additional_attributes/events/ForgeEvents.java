package de.cadentem.additional_attributes.events;

import de.cadentem.additional_attributes.registry.AAttributes;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void amplyLootingLevel(final LootingLevelEvent event) {
        event.setLootingLevel(AAttributes.getIntValue(event.getEntity(), AAttributes.LOOTING.get(), event.getLootingLevel()));
    }
}
