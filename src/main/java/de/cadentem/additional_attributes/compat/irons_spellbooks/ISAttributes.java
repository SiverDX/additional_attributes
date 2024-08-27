package de.cadentem.additional_attributes.compat.irons_spellbooks;

import de.cadentem.additional_attributes.AA;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ISAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE.key(), AA.MODID);
    public static final List<Holder<Attribute>> INNATE_ATTRIBUTES = new ArrayList<>();

    public static final String SEPARATOR = "/";
    public static final String SPELL_PREFIX = "spell" + SEPARATOR;
    public static final String SCHOOL_PREFIX = "school" + SEPARATOR;
    public static final String INNATE_SPELL_PREFIX = "innate_spell" + SEPARATOR;
    public static final String INNATE_SPELL_DESCRIPTION_PREFIX = "attribute." + AA.MODID + "." + INNATE_SPELL_PREFIX;
    public static final String INNATE_SCHOOL_PREFIX = "innate_school" + SEPARATOR;
    public static final String INNATE_SCHOOL_DESCRIPTION_PREFIX = "attribute." + AA.MODID + "." + INNATE_SCHOOL_PREFIX;

    public static final int LIMIT = 100;

    public static DeferredHolder<Attribute, Attribute> KEEP_SCROLL = ATTRIBUTES.register("keep_scroll", () -> new RangedAttribute("attribute." + AA.MODID + ".keep_scroll", 0, 0, 1).setSyncable(true));

    static {
        createAttribute("spell_general");
    }

    public static Holder<Attribute> getInnateSpell(final ResourceLocation resource) {
        return BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(ResourceKey.create(BuiltInRegistries.ATTRIBUTE.key(), ResourceLocation.fromNamespaceAndPath(AA.MODID, INNATE_SPELL_PREFIX + resource.getNamespace() + SEPARATOR + resource.getPath())));
    }

    public static Holder<Attribute> getInnateSchool(final ResourceLocation resource) {
        return BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(ResourceKey.create(BuiltInRegistries.ATTRIBUTE.key(), ResourceLocation.fromNamespaceAndPath(AA.MODID, INNATE_SCHOOL_PREFIX + resource.getNamespace() + SEPARATOR + resource.getPath())));
    }

    public static void createAttribute(final String id) {
        ATTRIBUTES.register(id, () -> new RangedAttribute("attribute." + AA.MODID + "." + id, 0, 0, LIMIT).setSyncable(true));
        AA.LOG.debug("Registered attribute [{}]", id);
    }

    public static void registerAttribute(final String id) {
        Attribute attribute = new RangedAttribute("attribute." + AA.MODID + "." + id, 0, 0, LIMIT).setSyncable(true);
        INNATE_ATTRIBUTES.add(Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(AA.MODID, id), attribute));
        AA.LOG.debug("Registered attribute [{}]", id);
    }

    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> {
            if (attribute.getId().getPath().startsWith(SCHOOL_PREFIX) || attribute.getId().getPath().startsWith(SPELL_PREFIX)) {
                event.getTypes().forEach(type -> event.add(type, attribute));
            } else {
                event.add(EntityType.PLAYER, attribute);
            }

            if (((InnateAttribute) attribute).additional_attributes$isInnateAttribute()) {
                INNATE_ATTRIBUTES.add(attribute);
            }
        });
    }

    public static ResourceLocation getLocation(final Holder<Attribute> attribute, final String prefix) {
        // FIXME 1.21 :: does name still match?
        return ResourceLocation.tryParse(attribute.getRegisteredName().replace(prefix, "").replaceFirst(ISAttributes.SEPARATOR, ":"));
    }
}
