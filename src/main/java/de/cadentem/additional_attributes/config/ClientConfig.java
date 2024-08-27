package de.cadentem.additional_attributes.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SPEC;

    public static ModConfigSpec.BooleanValue SKIP_INNATE;

    static {
        SKIP_INNATE = BUILDER.comment("If enabled innate spells won't be added to the selection if an equally or more powerful spell of the same type is already present").define("skip_innate", false);

        SPEC = BUILDER.build();
    }
}
