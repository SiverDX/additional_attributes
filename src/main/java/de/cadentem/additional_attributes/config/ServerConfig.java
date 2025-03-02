package de.cadentem.additional_attributes.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue ALLOW_MAX_LEVEL_ONE_INCREASES;
    public static ForgeConfigSpec.BooleanValue INNATE_UNLOCKS_ELDRITCH;

    public static ForgeConfigSpec.IntValue MAX_RARITY;
    public static ForgeConfigSpec.IntValue SHIFT_MIN_RARITY;

    static {
        BUILDER.push("Iron's Spells 'n Spellbooks");
        ALLOW_MAX_LEVEL_ONE_INCREASES = BUILDER.comment("Allow level increases for spells with a max. level of 1").define("allow_max_level_one_increase", false);
        INNATE_UNLOCKS_ELDRITCH = BUILDER.comment("Innate eldritch spells will be unlocked by default if enabled").define("innate_unlocks_eldritch", false);
        BUILDER.pop();

        BUILDER.push("Apotheosis");
        MAX_RARITY = BUILDER.comment("Max. rarity that can be crafted (6 = ancient)").defineInRange("max_craftable_rarity", 6, 0, 10);
        String firstLine = "Shifts the min. rarity downwards by this value\n";
        String secondLine = "Example with a value of 2 and attribute value of 3.5: Chance between common and epic rarity\n";
        String thirdLine = "(Without a shift this would be a chance between rare and epic rarity)";
        SHIFT_MIN_RARITY = BUILDER.comment(firstLine + secondLine + thirdLine).defineInRange("shift_min_rarity", 0, 0, 10);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
