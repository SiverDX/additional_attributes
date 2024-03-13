package de.cadentem.additional_attributes.mixin;

import de.cadentem.additional_attributes.registry.AAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyVariable(method = "decreaseAirSupply", at = @At(value = "STORE"), ordinal = 1)
    private int additional_attributes$amplifyValue(int original) {
        LivingEntity instance = (LivingEntity) (Object) this;

        if (instance instanceof Player player) {
            return AAttributes.getIntValue(player, AAttributes.RESPIRATION.get(), original);
        }

        return original;
    }
}
