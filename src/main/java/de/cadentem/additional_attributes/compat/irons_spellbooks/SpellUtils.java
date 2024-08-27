package de.cadentem.additional_attributes.compat.irons_spellbooks;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.config.ServerConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpellUtils {
    public static int calculateSpellLevel(@Nullable final LivingEntity livingEntity, final AbstractSpell spell, int originalLevel) {
        if (livingEntity == null) {
            return originalLevel;
        }

        ResourceLocation spellResource = spell.getSpellResource();
        ResourceLocation schoolResource = spell.getSchoolType().getId();

        Holder<Attribute> schoolAttribute = BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(ResourceKey.create(BuiltInRegistries.ATTRIBUTE.key(), ResourceLocation.fromNamespaceAndPath(AA.MODID, ISAttributes.SCHOOL_PREFIX + schoolResource.getNamespace() + ISAttributes.SEPARATOR + schoolResource.getPath())));
        Holder<Attribute> spellAttribute = BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(ResourceKey.create(BuiltInRegistries.ATTRIBUTE.key(), ResourceLocation.fromNamespaceAndPath(AA.MODID, ISAttributes.SPELL_PREFIX + spellResource.getNamespace() + ISAttributes.SEPARATOR + spellResource.getPath())));

        List<AttributeModifier> addition = new ArrayList<>();
        List<AttributeModifier> multiplyBase = new ArrayList<>();
        List<AttributeModifier> multiplyTotal = new ArrayList<>();

        fillModifiers(livingEntity, ISAttributes.SPELL_GENERAL, addition, multiplyBase, multiplyTotal);
        fillModifiers(livingEntity, schoolAttribute, addition, multiplyBase, multiplyTotal);
        fillModifiers(livingEntity, spellAttribute, addition, multiplyBase, multiplyTotal);

        double base = originalLevel;

        for (AttributeModifier modifier : addition) {
            base += modifier.amount();
        }

        double result = base;

        for (AttributeModifier modifier : multiplyBase) {
            result += base * modifier.amount();
        }

        for (AttributeModifier modifier : multiplyTotal) {
            result *= 1 + modifier.amount();
        }

        if (spell.getMaxLevel() == 1 && result > 1 && !ServerConfig.ALLOW_MAX_LEVEL_ONE_INCREASES.get()) {
            // Normally this means the spell does not scale with level
            return originalLevel;
        }

        return (int) Mth.clamp(result, 0, Math.max(ISAttributes.LIMIT, originalLevel));
    }

    private static void fillModifiers(final LivingEntity livingEntity, final Holder<Attribute> attribute, final List<AttributeModifier> addition, final List<AttributeModifier> multiplyBase, final List<AttributeModifier> multiplyTotal) {
        if (livingEntity == null || attribute == null) {
            return;
        }

        AttributeInstance instance = livingEntity.getAttribute(attribute);

        if (instance == null) {
            return;
        }

        for (AttributeModifier modifier : instance.getModifiers()) {
            switch (modifier.operation()) {
                case ADD_VALUE -> addition.add(modifier);
                case ADD_MULTIPLIED_BASE -> multiplyBase.add(modifier);
                case ADD_MULTIPLIED_TOTAL -> multiplyTotal.add(modifier);
            }
        }
    }
}
