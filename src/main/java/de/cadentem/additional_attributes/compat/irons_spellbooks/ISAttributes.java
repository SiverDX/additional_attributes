package de.cadentem.additional_attributes.compat.irons_spellbooks;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.datagen.AALanguageProvider;
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
    public static final List<Holder<Attribute>> ATTRIBUTE_ENTRIES = new ArrayList<>();

    public static final String SEPARATOR = "/";
    public static final String NAME_PREFIX = AA.MODID + ":";
    public static final String SPELL_PREFIX = "spell" + SEPARATOR;
    public static final String SCHOOL_PREFIX = "school" + SEPARATOR;
    public static final String INNATE_SPELL_PREFIX = "innate_spell" + SEPARATOR;
    public static final String INNATE_SPELL_NAME_PREFIX = NAME_PREFIX + INNATE_SPELL_PREFIX;
    public static final String INNATE_SCHOOL_PREFIX = "innate_school" + SEPARATOR;
    public static final String INNATE_SCHOOL_NAME_PREFIX = NAME_PREFIX + INNATE_SCHOOL_PREFIX;

    public static final int LIMIT = 100;

    public static DeferredHolder<Attribute, Attribute> KEEP_SCROLL = ATTRIBUTES.register("keep_scroll", () -> new RangedAttribute(AALanguageProvider.PREFIX + "keep_scroll", 0, 0, 1).setSyncable(true));
    public static DeferredHolder<Attribute, Attribute> SPELL_GENERAL = ATTRIBUTES.register("spell_general", () -> new RangedAttribute(AALanguageProvider.PREFIX + "spell_general", 0, 0, LIMIT).setSyncable(true));

    public static Holder<Attribute> getInnateSpell(final ResourceLocation resource) {
        return BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(ResourceKey.create(BuiltInRegistries.ATTRIBUTE.key(), ResourceLocation.fromNamespaceAndPath(AA.MODID, INNATE_SPELL_PREFIX + resource.getNamespace() + SEPARATOR + resource.getPath())));
    }

    public static Holder<Attribute> getInnateSchool(final ResourceLocation resource) {
        return BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(ResourceKey.create(BuiltInRegistries.ATTRIBUTE.key(), ResourceLocation.fromNamespaceAndPath(AA.MODID, INNATE_SCHOOL_PREFIX + resource.getNamespace() + SEPARATOR + resource.getPath())));
    }

    public static void registerAttribute(final String id) {
        Attribute attribute = new RangedAttribute(AALanguageProvider.PREFIX + id, 0, 0, LIMIT).setSyncable(true);
        ATTRIBUTE_ENTRIES.add(Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(AA.MODID, id), attribute));
        AA.LOG.debug("Registered attribute [{}]", id);
    }

    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> {
            if (attribute == SPELL_GENERAL) {
                event.getTypes().forEach(type -> event.add(type, attribute));
            } else {
                event.add(EntityType.PLAYER, attribute);
            }
        });

        for (Holder<Attribute> attribute : ATTRIBUTE_ENTRIES) {
            // These attributes were not registered through the deferred registry
            String id = attribute.getRegisteredName().replace(AALanguageProvider.PREFIX, "");

            if (id.startsWith(SCHOOL_PREFIX) || id.startsWith(SPELL_PREFIX)) {
                event.getTypes().forEach(type -> event.add(type, attribute));
            } else {
                event.add(EntityType.PLAYER, attribute);
            }
        }

        // Only need to keep the innate attributes to update the spell selection
        ATTRIBUTE_ENTRIES.removeIf(attribute -> !((InnateAttribute) attribute.value()).additional_attributes$isInnateAttribute());
    }

    public static ResourceLocation getLocation(final Holder<Attribute> attribute, final String prefix) {
        return ResourceLocation.tryParse(attribute.getRegisteredName().replace(prefix, "").replaceFirst(ISAttributes.SEPARATOR, ":"));
    }
}
