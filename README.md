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

# Apotheosis
## Apothic Crafting
An attribute that adds a chance to affix crafted items

Logic works as follows (using the baseline `Apotheosis` rarities):
- **0 to 1**: Chance between nothing happening and `common` rarity
- **1 to 2**: Chance between `common` and `uncommon` rarity
- **2 to 3**: Chance between `uncommon` and `rare` rarity
- **3 to 4**: Chance between `rare` and `epic` rarity
- **4 to 5**: Chance between `epic` and `mythic` rarity
- **5 to 6**: Chance between `mythic` and `ancient` rarity
- **6 to ?**    : Can only roll `ancient` rarity

If the attribute value is exactly 0 nothing will happen

A value of `1.3` has a 30% chance to consider `uncommon` rarity - The `luck` attribute will still affect the actually picked rarity

There is a server config that can limit the max. crafted rarity

# Iron's Spells 'n Spellbooks
## Increase or decrease spell levels
- General attribute (meaning all spells) (`additional_attributes:spell_general`)
- Attribute for each school type (`additional_attributes:spell_school_<...>`)
  - Example: `additional_attributes:spell_school_fire`
  - Third-party schools have the syntax `additional_attributes:school/<namespace>/<path>`
- Attribute for each spell type (`additional_attributes:spell_type_<...>`)
  - Example: `additional_attributes:spell_type_shockwave`
  - Third-party spells have the syntax `additional_attributes:spell/<namespace>/<path>`
    - Example: `additional_attributes:spell/traveloptics/orbital_void`

How it works:
- The base value of these attributes is the level of the spell being used - meaning `modify_base` will always have an effect
- Spells with a maximum level of `1` won't have their spell level increased, since there usually is no reason to do so
- The three attribute types function as one - their modifiers are gathered together before calculation
- There is no rounding - meaning a spell level of `1.75` will result in `1`
- If the spell level reaches 0 (due to negative modifiers) the spell cannot be cast
- These attributes are present for all entities not just the player

## Add spells to the spell selection
These spells will be available independent of the current spellbook, weapon, etc.
- Attribute for each school type (`additional_attributes:innate_school/<namespace>/<path>`)
  - Example: `additional_attributes:innate_school/irons_spellbooks/fire`
- Attribute for each spell type (`additional_attributes:innate_spell/<namespace>/<path>`) 
  - Example: `additional_attributes:innate_spell/irons_spellbooks/cloud_of_regeneration`

## Other
Added an attribute to potentially not use up a scroll
- ID is `additional_attributes:keep_scroll`
- Value between `0` (`0%` chance) and 1 (`100%` chance)
- Using `multiply_base` will have no effect since the base value is `0`
- Using `multiply_total` without prior `addition` will also have no effect

---

References:
- School Types https://github.com/iron431/Irons-Spells-n-Spellbooks/blob/1.19.2/src/main/java/io/redspace/ironsspellbooks/api/registry/SchoolRegistry.java
- Spell Types https://github.com/iron431/Irons-Spells-n-Spellbooks/blob/1.19.2/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java