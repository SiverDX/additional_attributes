package de.cadentem.additional_attributes.utils;

import de.cadentem.additional_attributes.mixin.EntityAccess;
import de.cadentem.additional_attributes.mixin.FishingHookAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.FishingHook;

public class Utils {
    /** <a href="https://github.com/Shadows-of-Fire/Apotheosis/blob/1.20/src/main/java/dev/shadowsoffire/apotheosis/ench/asm/EnchHooks.java">Source</a> */
    public static int getTicksCaughtDelay(final FishingHook bobber) {
        FishingHookAccess hookAccess = (FishingHookAccess) bobber;
        int lowBound = Math.max(1, 100 - hookAccess.getLureSpeed() * 10);
        int highBound = Math.max(lowBound, 600 - hookAccess.getLureSpeed() * 60);
        return Mth.nextInt(((EntityAccess) bobber).getRandom(), lowBound, highBound);
    }
}
