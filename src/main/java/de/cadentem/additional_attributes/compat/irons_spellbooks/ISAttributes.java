package de.cadentem.additional_attributes.compat.irons_spellbooks;

import de.cadentem.additional_attributes.AA;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;

public class ISAttributes {
    public static HashMap<Player, Boolean> SHOULD_RELOAD = new HashMap<>();

    public static List<Attribute> INNATE_SCHOOLS;
    public static List<Attribute> INNATE_SPELLS;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, AA.MODID);

    public static final String SEPARATOR = "/";
    public static final String DESCRIPTION_PREFIX = "attribute." + AA.MODID + ".";
    public static final String SPELL_PREFIX = "spell_type_";
    public static final String SCHOOL_PREFIX = "spell_school_";
    public static final String INNATE_SPELL_PREFIX = "innate_spell/";
    public static final String INNATE_SCHOOL_PREFIX = "innate_spell/";

    public static final int LIMIT = 100;

    public static RegistryObject<Attribute> KEEP_SCROLL = ATTRIBUTES.register("keep_scroll", () -> new RangedAttribute("attribute." + AA.MODID + ".keep_scroll", 0, 0, 1).setSyncable(true));

    static {
        createAttribute("spell_general");
    }

    public static void createAttribute(final String id) {
        ATTRIBUTES.register(id, () -> new RangedAttribute("attribute." + AA.MODID + "." + id, 0, 0, LIMIT).setSyncable(true));
        AA.LOG.debug("Registered attribute [{}]", id);
    }

    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> event.add(EntityType.PLAYER, attribute.get()));
    }

    public static boolean areInnateListsMissing() {
        return INNATE_SCHOOLS == null || INNATE_SPELLS == null;
    }

    public static void initInnateLists() {
        ISAttributes.INNATE_SCHOOLS = ISAttributes.ATTRIBUTES.getEntries().stream()
                .filter(attribute -> attribute.get().getDescriptionId().startsWith(ISAttributes.DESCRIPTION_PREFIX + ISAttributes.INNATE_SCHOOL_PREFIX))
                .map(RegistryObject::get).toList();

        ISAttributes.INNATE_SPELLS = ISAttributes.ATTRIBUTES.getEntries().stream()
                .filter(attribute -> attribute.get().getDescriptionId().startsWith(ISAttributes.DESCRIPTION_PREFIX + ISAttributes.INNATE_SPELL_PREFIX))
                .map(RegistryObject::get).toList();
    }
}
