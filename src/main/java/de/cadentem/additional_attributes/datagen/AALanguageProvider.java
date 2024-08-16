package de.cadentem.additional_attributes.datagen;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

public class AALanguageProvider extends LanguageProvider {
    public AALanguageProvider(final PackOutput output, final String locale) {
        super(output, AA.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("ui." + AA.MODID + ".cast_error_no_spell_level", "You do not possess the knowledge required to cast this spell");

        add("attribute." + AA.MODID + ".fishing_lure", "Fishing Lure");
        add("attribute." + AA.MODID + ".fishing_lure.desc", "Modifies the fishing lure level of the player");
        add("attribute." + AA.MODID + ".fishing_luck", "Fishing Luck");
        add("attribute." + AA.MODID + ".fishing_luck.desc", "Modifies the fishing luck level of the player");
        add("attribute." + AA.MODID + ".looting", "Looting");
        add("attribute." + AA.MODID + ".looting.desc", "Modifies the looting level of the player");
        add("attribute." + AA.MODID + ".respiration", "Respiration");
        add("attribute." + AA.MODID + ".respiration.desc", "Modifies the respiration level of the player");
        add("attribute." + AA.MODID + ".harvest", "Harvesting");
        add("attribute." + AA.MODID + ".harvest.desc", "Modifies the amount of harvested crops");

        add("attribute." + AA.MODID + ".keep_scroll", "Keep Scroll");
        add("attribute." + AA.MODID + ".keep_scroll.desc", "Chance to not use up a spell scroll");

        ISAttributes.ATTRIBUTES.getEntries().forEach(attribute -> {
            if (attribute == ISAttributes.KEEP_SCROLL) {
                return;
            }

            ResourceLocation location = attribute.getKey().location();
            String path = location.getPath();
            StringBuilder readable = new StringBuilder();

            if (path.contains("spell_general")) {
                readable.append("General Spell Level");
                add("attribute." + location.toLanguageKey(), readable.toString());
                add("attribute." + location.toLanguageKey() + ".desc", "Modifies the level of all spells");

                return;
            }

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
                    if (path.contains("spell_school")) {
                        add("attribute." + location.toLanguageKey(), readable.append(" School Level").toString());
                        add("attribute." + location.toLanguageKey() + ".desc", "Modifies the level of all spells of this school");
                    } else if (path.contains("spell_type")) {
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
