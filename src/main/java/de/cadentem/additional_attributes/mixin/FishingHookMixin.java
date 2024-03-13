package de.cadentem.additional_attributes.mixin;

import de.cadentem.additional_attributes.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.FishingHook;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Inject(method = "catchingFish", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/entity/projectile/FishingHook;timeUntilLured:I", ordinal = 3, shift = At.Shift.AFTER))
    private void additional_attributes$fixLure(final BlockPos position, final CallbackInfo callback) {
        FishingHook instance = (FishingHook) (Object) this;
        ((FishingHookAccess) instance).setTimeUntilLured(Utils.getTicksCaughtDelay(instance));
    }
}