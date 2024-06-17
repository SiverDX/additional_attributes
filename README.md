# Enchantment Attributes
- Looting (`additional_attributes:looting`)
- Respiration (`additional_attributes:respiration`)
- Fishing Lure (`additional_attributes:fishing_lure`)
- Fishing Luck (`additional_attributes:fishing_lure`)

The base value for these is the enchantment value which gets checked
- Meaning there won't be any benefits using `multiply_base` / `multiply_total` if you don't have the enchantment
- `addition` will be affected by `multiply_total`

# Harvest Attribute
Can be used to increase the amount of harvested items
- ID is `additional_attributes:harvest`
- `addition` of `1` means one additional crop (not seed)
- The base value is the initial harvested amount

# Iron's Spells 'n Spellbooks
Added attributes to increase (or lower) spell levels
- General attribute (meaning all spells) (`additional_attributes:spell_general`)
- Attribute for each school type (`additional_attributes:spell_school_<...>`) (e.g. `additional_attributes:spell_school_fire`)
- Attribute for each spell type (`additional_attributes:spell_type_<...>`) (e.g. `additional_attributes:spell_type_shockwave`)

How it works:
- The base value of these attributes is the level of the spell being used - meaning `multiply_base` will always have an effect
- Spells with a maximum level of `1` won't have their spell level increased, since there usually is no reason to do so
- The three attribute types function as one - their modifiers are gathered together before calculation
- There is no rounding - meaning a spell level of `1.75` will result in `1`
- If the spell level reaches 0 (due to negative modifiers) the spell cannot be cast

---

Added an attribute to potentially not use up a scroll
- ID is `additional_attributes:keep_scroll`
- Value between `0` (`0%` chance) and 1 (`100%` chance)
- Using `multiply_base` will have no effect since the base value is `0`
- Using `multiply_total` without prior `addition` will also have no effect

---

References:
- School Types https://github.com/iron431/Irons-Spells-n-Spellbooks/blob/1.19.2/src/main/java/io/redspace/ironsspellbooks/api/registry/SchoolRegistry.java
- Spell Types https://github.com/iron431/Irons-Spells-n-Spellbooks/blob/1.19.2/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java
