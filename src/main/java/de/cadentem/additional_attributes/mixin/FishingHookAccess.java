package de.cadentem.additional_attributes.mixin;

import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FishingHook.class)
public interface FishingHookAccess {
    @Accessor("timeUntilLured")
    void setTimeUntilLured(int timeUntilLured);

    @Accessor("lureSpeed")
    int getLureSpeed();
}
