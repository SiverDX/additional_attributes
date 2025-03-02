package de.cadentem.additional_attributes.datagen;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class AALanguageProvider extends LanguageProvider {
    public static final String PREFIX = "attribute." + AA.MODID + ".";

    private static final List<String> SKIP = List.of(
            "fishing_lure",
            "fishing_luck",
            "looting",
            "respiration",
            "harvest",
            "keep_scroll",
            "spell_general"
    );

    public AALanguageProvider(final PackOutput output, final String locale) {
        super(output, AA.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("ui." + AA.MODID + ".cast_error_no_spell_level", "You do not possess the knowledge required to cast this spell");

        add(PREFIX + "fishing_lure", "Fishing Lure");
        add(PREFIX + "fishing_lure.desc", "Modifies the fishing lure level of the player");
        add(PREFIX + "fishing_luck", "Fishing Luck");
        add(PREFIX + "fishing_luck.desc", "Modifies the fishing luck level of the player");
        add(PREFIX + "looting", "Looting");
        add(PREFIX + "looting.desc", "Modifies the looting level of the player");
        add(PREFIX + "respiration", "Respiration");
        add(PREFIX + "respiration.desc", "Modifies the respiration level of the player");
        add(PREFIX + "harvest", "Harvesting");
        add(PREFIX + "harvest.desc", "Modifies the amount of harvested crops");

        add(PREFIX + "apothic_crafting", "Apothic Crafting");
        add(PREFIX + "apothic_crafting.desc", "Grants the chance to craft affixed items (min. affix rarity will increase in increments of 1)");

        add(PREFIX + "keep_scroll", "Keep Scroll");
        add(PREFIX + "keep_scroll.desc", "Chance to not use up a spell scroll");
        add(PREFIX + "spell_general", "General Spell Level");
        add(PREFIX + "spell_general.desc", "Modifies the level of all spells");

        ForgeRegistries.ATTRIBUTES.getEntries().forEach(attribute -> {
            ResourceLocation location = attribute.getKey().location();

            if (!location.getNamespace().equals(AA.MODID) || SKIP.contains(location.getPath())) {
                return;
            }

            String path = location.getPath();
            StringBuilder readable = new StringBuilder();

            if (handleInnateAttribute(location)) {
                return;
            }

            String[] split = path.split("_");

            // Convert the technical name (e.g. cloud_of_regeneration) to a more readable one (e.g. Cloud Of Regeneration)
            for (/* Skip prefix (e.g. `spell_type_`) */ int i = 2; i < split.length; i++) {
                readable.append(Character.toUpperCase(split[i].charAt(0)));
                readable.append(split[i].substring(1));

                if (i != split.length - 1) {
                    readable.append(" ");
                } else {
                    if (path.startsWith(ISAttributes.SCHOOL_PREFIX)) {
                        add("attribute." + location.toLanguageKey(), readable.append(" School Level").toString());
                        add("attribute." + location.toLanguageKey() + ".desc", "Modifies the level of all spells of this school");
                    } else if (path.startsWith(ISAttributes.SPELL_PREFIX)) {
                        add("attribute." + location.toLanguageKey(), readable.append(" Spell Level").toString());
                        add("attribute." + location.toLanguageKey() + ".desc", "Modifies the level of this spell");
                    }
                }
            }
        });
    }

    private boolean handleInnateAttribute(final ResourceLocation location) {
        boolean isSchool = location.getPath().startsWith(ISAttributes.INNATE_SCHOOL_PREFIX);
        boolean isSpell = location.getPath().startsWith(ISAttributes.INNATE_SPELL_PREFIX);

        if (!isSchool && !isSpell) {
            return false;
        }

        String[] elements = location.toString().split("/");
        // 0: The prefix
        // 1: the namespace
        String[] nameParts = elements[2].split("_");
        StringBuilder name = new StringBuilder();

        for (int i = 0; i < nameParts.length; i++) {
            String namePart = nameParts[i];
            // Convert the technical name (e.g. cloud_of_regeneration) to a more readable one (e.g. Cloud Of Regeneration)
            name.append(Character.toUpperCase(namePart.charAt(0))).append(namePart.substring(1));

            if (i != nameParts.length - 1) {
                name.append(" ");
            }
        }

        if (isSchool) {
            add("attribute." + location.toLanguageKey(), "Innate School: " + name);
            add("attribute." + location.toLanguageKey() + ".desc", "Grants all spells of this school (level is based on attribute value)");
        } else {
            add("attribute." + location.toLanguageKey(), "Innate Spell: " + name);
            add("attribute." + location.toLanguageKey() + ".desc", "Grants this spell (level is based on attribute value)");
        }

        return true;
    }
}
