package de.cadentem.additional_attributes;

import com.mojang.logging.LogUtils;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISEvents;
import de.cadentem.additional_attributes.config.ServerConfig;
import de.cadentem.additional_attributes.registry.AALootModifiers;
import de.cadentem.additional_attributes.registry.AAttributes;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(AA.MODID)
public class AA {
    public static final String MODID = "additional_attributes";
    public static final Logger LOG = LogUtils.getLogger();

    public AA(final IEventBus bus, final ModContainer container) {
        AALootModifiers.LOOT_MODIFIERS.register(bus);
        AAttributes.ATTRIBUTES.register(bus);

        if (ModList.get().isLoaded("irons_spellbooks")) {
            bus.addListener(EventPriority.LOWEST, ISEvents::registerAttributes);
            bus.addListener(ISAttributes::setAttributes);
            ISAttributes.ATTRIBUTES.register(bus);
            NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ISEvents::modifyLevel);
            NeoForge.EVENT_BUS.addListener(ISEvents::modifySpellSelection);
        }

        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
