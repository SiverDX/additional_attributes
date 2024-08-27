package de.cadentem.additional_attributes.datagen;

import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void configureDataGen(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new AALanguageProvider(generator.getPackOutput(), "en_us"));
        generator.addProvider(event.includeServer(), new AALootModifiers(generator.getPackOutput(), event.getLookupProvider()));
    }
}
