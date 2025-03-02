package de.cadentem.additional_attributes.compat.irons_spellbooks;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.datagen.AALanguageProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ISAttributes {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, AA.MODID);
    public static final List<Attribute> ATTRIBUTE_ENTRIES = new ArrayList<>();

    public static final String SEPARATOR = "/";
    public static final String SPELL_PREFIX = "spell_type_";
    public static final String SPELL_PREFIX_NEW = "spell" + SEPARATOR;
    public static final String SCHOOL_PREFIX = "spell_school_";
    public static final String SCHOOL_PREFIX_NEW = "school" + SEPARATOR;
    public static final String INNATE_SPELL_PREFIX = "innate_spell" + SEPARATOR;
    public static final String INNATE_SPELL_DESCRIPTION_PREFIX = AALanguageProvider.PREFIX + INNATE_SPELL_PREFIX;
    public static final String INNATE_SCHOOL_PREFIX = "innate_school" + SEPARATOR;
    public static final String INNATE_SCHOOL_DESCRIPTION_PREFIX = AALanguageProvider.PREFIX + INNATE_SCHOOL_PREFIX;

    public static final int LIMIT = 100;

    public static RegistryObject<Attribute> KEEP_SCROLL = REGISTRY.register("keep_scroll", () -> new RangedAttribute(AALanguageProvider.PREFIX + "keep_scroll", 0, 0, 1).setSyncable(true));
    public static RegistryObject<Attribute> SPELL_GENERAL = REGISTRY.register("spell_general", () -> new RangedAttribute(AALanguageProvider.PREFIX + "spell_general", 0, 0, LIMIT).setSyncable(true));

    public static @Nullable Attribute getInnateSpell(final ResourceLocation resource) {
        return ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryBuild(AA.MODID, INNATE_SPELL_PREFIX + resource.getNamespace() + SEPARATOR + resource.getPath()));
    }

    public static @Nullable Attribute getInnateSchool(final ResourceLocation resource) {
        return ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryBuild(AA.MODID, INNATE_SCHOOL_PREFIX + resource.getNamespace() + SEPARATOR + resource.getPath()));
    }

    public static void registerAttribute(final String id) {
        Attribute attribute = new RangedAttribute(AALanguageProvider.PREFIX + id, 0, 0, LIMIT).setSyncable(true);
        ForgeRegistries.ATTRIBUTES.register(id, attribute);
        ATTRIBUTE_ENTRIES.add(attribute);
        AA.LOG.debug("Registered attribute [{}]", id);
    }

    public static void setAttributes(final EntityAttributeModificationEvent event) {
        REGISTRY.getEntries().forEach(attribute -> {
            if (attribute == SPELL_GENERAL) {
                event.getTypes().forEach(type -> event.add(type, attribute.get()));
            } else {
                event.add(EntityType.PLAYER, attribute.get());
            }
        });

        for (Attribute attribute : ATTRIBUTE_ENTRIES) {
            // These attributes were not registered through the deferred registry
            String id = attribute.getDescriptionId().replace(AALanguageProvider.PREFIX, "");

            if (id.startsWith(SCHOOL_PREFIX) || id.startsWith(SCHOOL_PREFIX_NEW) || id.startsWith(SPELL_PREFIX) || id.startsWith(SPELL_PREFIX_NEW)) {
                event.getTypes().forEach(type -> event.add(type, attribute));
            } else {
                event.add(EntityType.PLAYER, attribute);
            }
        }

        // Only need to keep the innate attributes to update the spell selection
        ATTRIBUTE_ENTRIES.removeIf(attribute -> !((InnateAttribute) attribute).additional_attributes$isInnateAttribute());
    }

    public static ResourceLocation getLocation(final Attribute attribute, final String prefix) {
        return ResourceLocation.tryParse(attribute.getDescriptionId().replace(prefix, "").replaceFirst(ISAttributes.SEPARATOR, ":"));
    }
}
