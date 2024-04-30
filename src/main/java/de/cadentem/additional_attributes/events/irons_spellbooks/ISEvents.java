package de.cadentem.additional_attributes.events.irons_spellbooks;

import de.cadentem.additional_attributes.utils.SpellUtils;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;

public class ISEvents {
    public static void modifyLevel(final ModifySpellLevelEvent event) {
        event.setLevel(SpellUtils.calculateSpellLevel(event.getEntity(), event.getSpell(), event.getLevel()));
    }
}
