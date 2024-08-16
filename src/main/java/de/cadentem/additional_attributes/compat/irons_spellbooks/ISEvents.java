package de.cadentem.additional_attributes.compat.irons_spellbooks;

import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ISEvents {
    public static void modifyLevel(final ModifySpellLevelEvent event) {
        event.setLevel(SpellUtils.calculateSpellLevel(event.getEntity(), event.getSpell(), event.getLevel()));
    }

    public static void modifySpellSelection(final SpellSelectionManager.SpellSelectionEvent event) {
        HashMap<AbstractSpell, Pair<Double, HashMap<AttributeModifier.Operation, Set<AttributeModifier>>>> spellModifiers = new HashMap<>();

        ISAttributes.ATTRIBUTES.getEntries().stream().filter(attribute -> ((InnateAttribute) attribute.get()).additional_attributes$isInnateAttribute()).map(RegistryObject::get).forEach(attribute -> {
            AttributeInstance instance = event.getEntity().getAttribute(attribute);

            if (instance == null) {
                return;
            }

            if (attribute.getDescriptionId().startsWith(ISAttributes.INNATE_SCHOOL_DESCRIPTION_PREFIX)) {
                SchoolType school = SchoolRegistry.REGISTRY.get().getValue(ISAttributes.getLocation(attribute, ISAttributes.INNATE_SCHOOL_DESCRIPTION_PREFIX));

                for (AbstractSpell spell : SpellRegistry.REGISTRY.get().getValues()) {
                    if (spell.getSchoolType() == school) {
                        addModifiers(spellModifiers, instance, spell);
                    }
                }
            } else if (attribute.getDescriptionId().startsWith(ISAttributes.INNATE_SPELL_DESCRIPTION_PREFIX)) {
                AbstractSpell spell = SpellRegistry.REGISTRY.get().getValue(ISAttributes.getLocation(attribute, ISAttributes.INNATE_SPELL_DESCRIPTION_PREFIX));
                addModifiers(spellModifiers, instance, spell);
            }
        });

        int index = 0;

        for (AbstractSpell spell : spellModifiers.keySet()) {
            Pair<Double, HashMap<AttributeModifier.Operation, Set<AttributeModifier>>> data = spellModifiers.get(spell);
            HashMap<AttributeModifier.Operation, Set<AttributeModifier>> modifiers = data.right();

            double base = data.left();

            for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.ADDITION, Collections.emptySet())) {
                base += modifier.getAmount();
            }

            double result = base;

            for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.MULTIPLY_BASE, Collections.emptySet())) {
                result += base * modifier.getAmount();
            }

            for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.MULTIPLY_TOTAL, Collections.emptySet())) {
                result *= 1 + modifier.getAmount();
            }

            int level = (int) result;

            if (level > 0) {
                event.addSelectionOption(new SpellData(spell, level), "innate_spells", index);
                index++;
            }
        }
    }

    private static void addModifiers(final HashMap<AbstractSpell, Pair<Double, HashMap<AttributeModifier.Operation, Set<AttributeModifier>>>> spellModifiers, final AttributeInstance instance, final AbstractSpell spell) {
        Pair<Double, HashMap<AttributeModifier.Operation, Set<AttributeModifier>>> data = spellModifiers.compute(spell, (key, value) -> {
            if (value == null) {
                return Pair.of(instance.getBaseValue(), new HashMap<>());
            }

            return Pair.of(value.left() + instance.getBaseValue(), value.right());
        });

        for (AttributeModifier.Operation operation : AttributeModifier.Operation.values()) {
            data.right().computeIfAbsent(operation, key -> new HashSet<>()).addAll(instance.getModifiers(operation));
        }
    }
}
