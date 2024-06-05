package de.cadentem.additional_attributes.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue ALLOW_MAX_LEVEL_ONE_INCREASES;

    static {
        ALLOW_MAX_LEVEL_ONE_INCREASES = BUILDER.comment("Allow level increases for spells with a max. level of 1").define("allow_max_level_one_increase", false);

        SPEC = BUILDER.build();
    }
}
