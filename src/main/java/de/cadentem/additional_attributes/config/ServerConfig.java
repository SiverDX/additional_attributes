package de.cadentem.additional_attributes.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public static ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SPEC;

    public static ModConfigSpec.BooleanValue ALLOW_MAX_LEVEL_ONE_INCREASES;
    public static ModConfigSpec.BooleanValue INNATE_UNLOCKS_ELDRITCH;
    public static ModConfigSpec.BooleanValue SKIP_INNATE;

    static {
        ALLOW_MAX_LEVEL_ONE_INCREASES = BUILDER.comment("Allow level increases for spells with a max. level of 1").define("allow_max_level_one_increase", false);
        INNATE_UNLOCKS_ELDRITCH = BUILDER.comment("Innate eldritch spells will be unlocked by default if enabled").define("innate_unlocks_eldritch", false);
        SKIP_INNATE = BUILDER.comment("If enabled innate spells won't be added to the selection if an equally or more powerful spell of the same type is already present").define("skip_innate", false);

        SPEC = BUILDER.build();
    }
}
