package de.cadentem.additional_attributes.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue SKIP_INNATE;

    static {
        SKIP_INNATE = BUILDER.comment("If enabled innate spells won't be added to the selection if an equally or more powerful spell of the same type is already present").define("skip_innate", false);

        SPEC = BUILDER.build();
    }
}
