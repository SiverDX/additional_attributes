package de.cadentem.additional_attributes.compat.irons_spellbooks;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.config.ServerConfig;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpellUtils {
    public static int calculateSpellLevel(@Nullable final LivingEntity livingEntity, final AbstractSpell spell, int originalLevel) {
        if (livingEntity == null) {
            return originalLevel;
        }

        Attribute schoolAttribute;
        Attribute spellAttribute;

        ResourceLocation spellResource = spell.getSpellResource();
        ResourceLocation schoolResource = spell.getSchoolType().getId();

        if (spellResource.getNamespace().equals(IronsSpellbooks.MODID)) {
            // To keep compatibility with previous versions
            schoolAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(AA.MODID, ISAttributes.SCHOOL_PREFIX + schoolResource.getPath()));
            spellAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(AA.MODID, ISAttributes.SPELL_PREFIX + spell.getSpellName()));
        } else {
            schoolAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(AA.MODID, ISAttributes.SCHOOL_PREFIX_NEW + schoolResource.getNamespace() + ISAttributes.SEPARATOR + schoolResource.getPath()));
            spellAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(AA.MODID, ISAttributes.SPELL_PREFIX_NEW + spellResource.getNamespace() + ISAttributes.SEPARATOR + spellResource.getPath()));
        }

        List<AttributeModifier> addition = new ArrayList<>();
        List<AttributeModifier> multiplyBase = new ArrayList<>();
        List<AttributeModifier> multiplyTotal = new ArrayList<>();

        fillModifiers(livingEntity, ISAttributes.SPELL_GENERAL.get(), addition, multiplyBase, multiplyTotal);
        fillModifiers(livingEntity, schoolAttribute, addition, multiplyBase, multiplyTotal);
        fillModifiers(livingEntity, spellAttribute, addition, multiplyBase, multiplyTotal);

        double base = originalLevel;

        for (AttributeModifier modifier : addition) {
            base += modifier.getAmount();
        }

        double result = base;

        for (AttributeModifier modifier : multiplyBase) {
            result += base * modifier.getAmount();
        }

        for (AttributeModifier modifier : multiplyTotal) {
            result *= 1 + modifier.getAmount();
        }

        if (spell.getMaxLevel() == 1 && result > 1 && !ServerConfig.ALLOW_MAX_LEVEL_ONE_INCREASES.get()) {
            // Normally this means the spell does not scale with level
            return originalLevel;
        }

        return (int) Mth.clamp(result, 0, Math.max(ISAttributes.LIMIT, originalLevel));
    }

    private static void fillModifiers(final LivingEntity livingEntity, final Attribute attribute, final List<AttributeModifier> addition, final List<AttributeModifier> multiplyBase, final List<AttributeModifier> multiplyTotal) {
        if (livingEntity == null || attribute == null) {
            return;
        }

        AttributeInstance instance = livingEntity.getAttribute(attribute);

        if (instance == null) {
            return;
        }

        addition.addAll(instance.getModifiers(AttributeModifier.Operation.ADDITION));
        multiplyBase.addAll(instance.getModifiers(AttributeModifier.Operation.MULTIPLY_BASE));
        multiplyTotal.addAll(instance.getModifiers(AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
