package de.cadentem.additional_attributes.utils;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SpellUtils {
    private static final ResourceLocation SPELL_GENERAL = new ResourceLocation(AA.MODID, "spell_general");

    public static int calculateSpellLevel(final Player player, final AbstractSpell spell, int originalLevel) {
        Attribute generalAttribute = ForgeRegistries.ATTRIBUTES.getValue(SPELL_GENERAL);
        Attribute schoolAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(AA.MODID, "spell_school_" + spell.getSchoolType().getId().getPath()));
        Attribute spellAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(AA.MODID, "spell_type_" + spell.getSpellName()));

        List<AttributeModifier> addition = new ArrayList<>();
        List<AttributeModifier> multiplyBase = new ArrayList<>();
        List<AttributeModifier> multiplyTotal = new ArrayList<>();

        fillModifiers(player, generalAttribute, addition, multiplyBase, multiplyTotal);
        fillModifiers(player, schoolAttribute, addition, multiplyBase, multiplyTotal);
        fillModifiers(player, spellAttribute, addition, multiplyBase, multiplyTotal);

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

        if (spell.getMaxLevel() == 1 && result > 1) {
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
