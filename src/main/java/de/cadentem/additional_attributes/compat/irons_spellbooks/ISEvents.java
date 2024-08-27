package de.cadentem.additional_attributes.compat.irons_spellbooks;

import de.cadentem.additional_attributes.config.ClientConfig;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.*;

public class ISEvents {
    public static void modifyLevel(final ModifySpellLevelEvent event) {
        event.setLevel(SpellUtils.calculateSpellLevel(event.getEntity(), event.getSpell(), event.getLevel()));
    }

    public static void modifySpellSelection(final SpellSelectionManager.SpellSelectionEvent event) {
        HashMap<AbstractSpell, Pair<Double, HashMap<AttributeModifier.Operation, Set<AttributeModifier>>>> spellModifiers = new HashMap<>();

        ISAttributes.ATTRIBUTE_ENTRIES.forEach(attribute -> {
            AttributeInstance instance = event.getEntity().getAttribute(attribute);

            if (instance == null) {
                return;
            }

            if (attribute.getRegisteredName().startsWith(ISAttributes.INNATE_SCHOOL_NAME_PREFIX)) {
                SchoolType school = SchoolRegistry.REGISTRY.get(ISAttributes.getLocation(attribute, ISAttributes.INNATE_SCHOOL_NAME_PREFIX));

                for (Map.Entry<ResourceKey<AbstractSpell>, AbstractSpell> spell : SpellRegistry.REGISTRY.entrySet()) {
                    if (spell.getValue().getSchoolType() == school) {
                        addModifiers(spellModifiers, instance, spell.getValue());
                    }
                }
            } else if (attribute.getRegisteredName().startsWith(ISAttributes.INNATE_SPELL_NAME_PREFIX)) {
                AbstractSpell spell = SpellRegistry.REGISTRY.get(ISAttributes.getLocation(attribute, ISAttributes.INNATE_SPELL_NAME_PREFIX));
                addModifiers(spellModifiers, instance, spell);
            }
        });

        int index = 0;

        for (AbstractSpell spell : spellModifiers.keySet()) {
            Pair<Double, HashMap<AttributeModifier.Operation, Set<AttributeModifier>>> data = spellModifiers.get(spell);
            HashMap<AttributeModifier.Operation, Set<AttributeModifier>> modifiers = data.right();

            double base = data.left();

            for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.ADD_VALUE, Collections.emptySet())) {
                base += modifier.amount();
            }

            double result = base;

            for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.ADD_MULTIPLIED_BASE, Collections.emptySet())) {
                result += base * modifier.amount();
            }

            for (AttributeModifier modifier : modifiers.getOrDefault(AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, Collections.emptySet())) {
                result *= 1 + modifier.amount();
            }

            int level = (int) result;

            if (level > 0) {
                boolean shouldAdd = true;

                if (ClientConfig.SKIP_INNATE.get()) {
                    List<SpellData> spellEntries = event.getManager().getAllSpells().stream().filter(option -> option.spellData.getSpell() == spell).map(option -> option.spellData).toList();

                    for (SpellData entry : spellEntries) {
                        if (entry.getLevel() >= level) {
                            shouldAdd = false;
                            break;
                        }
                    }
                }

                if (shouldAdd) {
                    event.addSelectionOption(new SpellData(spell, level), "innate_spells", index);
                    index++;
                }
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

        for (AttributeModifier modifier : instance.getModifiers()) {
            data.right().computeIfAbsent(modifier.operation(), key -> new HashSet<>()).add(modifier);
        }
    }

    public static void registerAttributes(final RegisterEvent event) {
        if (event.getRegistryKey() == SpellRegistry.REGISTRY.key()) {
            if (BuiltInRegistries.ATTRIBUTE instanceof MappedRegistry<Attribute> registry) {
                registry.unfreeze();

                SpellRegistry.REGISTRY.entrySet().forEach(spell -> {
                    ResourceLocation resource = spell.getKey().location();
                    ISAttributes.registerAttribute(ISAttributes.SPELL_PREFIX + resource.getNamespace() + ISAttributes.SEPARATOR + resource.getPath());
                    ISAttributes.registerAttribute(ISAttributes.INNATE_SPELL_PREFIX + resource.getNamespace() + ISAttributes.SEPARATOR + resource.getPath());
                });

                registry.freeze();
            }
        } else if (event.getRegistryKey() == SchoolRegistry.REGISTRY.key()) {
            if (BuiltInRegistries.ATTRIBUTE instanceof MappedRegistry<Attribute> registry) {
                registry.unfreeze();

                SchoolRegistry.REGISTRY.entrySet().forEach(school -> {
                    ResourceLocation resource = school.getKey().location();
                    ISAttributes.registerAttribute(ISAttributes.SCHOOL_PREFIX + resource.getNamespace() + ISAttributes.SEPARATOR + resource.getPath());
                    ISAttributes.registerAttribute(ISAttributes.INNATE_SCHOOL_PREFIX + resource.getNamespace() + ISAttributes.SEPARATOR + resource.getPath());
                });

                registry.freeze();
            }
        }
    }
}
