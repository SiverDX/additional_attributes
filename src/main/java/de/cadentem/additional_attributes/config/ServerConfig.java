package de.cadentem.additional_attributes.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue ALLOW_MAX_LEVEL_ONE_INCREASES;
    public static ForgeConfigSpec.BooleanValue INNATE_UNLOCKS_ELDRITCH;

    static {
        ALLOW_MAX_LEVEL_ONE_INCREASES = BUILDER.comment("Allow level increases for spells with a max. level of 1").define("allow_max_level_one_increase", false);
        INNATE_UNLOCKS_ELDRITCH = BUILDER.comment("Innate eldritch spells will be unlocked by default if enabled").define("innate_unlocks_eldritch", false);

        SPEC = BUILDER.build();
    }
}
