package de.cadentem.additional_attributes;

import com.mojang.logging.LogUtils;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import de.cadentem.additional_attributes.config.ServerConfig;
import de.cadentem.additional_attributes.events.irons_spellbooks.ISEvents;
import de.cadentem.additional_attributes.registry.AALootModifiers;
import de.cadentem.additional_attributes.registry.AAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AA.MODID)
public class AA {
    public static final String MODID = "additional_attributes";
    public static final Logger LOG = LogUtils.getLogger();

    public AA() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        AALootModifiers.LOOT_MODIFIERS.register(modEventBus);
        AAttributes.ATTRIBUTES.register(modEventBus);

        if (ModList.get().isLoaded("irons_spellbooks")) {
            modEventBus.addListener(ISAttributes::setAttributes);
            ISAttributes.ATTRIBUTES.register(modEventBus);
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, ISEvents::modifyLevel);
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
