package de.cadentem.additional_attributes.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void configureDataGen(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeClient(), new AALanguageProvider(generator.getPackOutput(), "en_us"));

        generator.addProvider(event.includeServer(), new AAItemTags(generator.getPackOutput(), event.getLookupProvider(), CompletableFuture.completedFuture(null), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new AALootModifiers(generator.getPackOutput()));
    }
}
