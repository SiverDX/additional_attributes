package de.cadentem.additional_attributes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.cadentem.additional_attributes.registry.AAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @ModifyReturnValue(method = "getFishingLuckBonus", at = @At("RETURN"))
    private static int additional_attributes$modifyFishingLuck(int original, @Local(argsOnly = true) final Entity entity) {
        if (entity instanceof Player player) {
            return AAttributes.getIntValue(player, AAttributes.FISHING_LUCK, original);
        }

        return original;
    }

    @ModifyReturnValue(method = "getFishingTimeReduction", at = @At("RETURN"))
    private static float additional_attributes$modifyFishingTimeReduction(float original, @Local(argsOnly = true) final Entity entity) {
        if (entity instanceof Player player) {
            return AAttributes.getIntValue(player, AAttributes.FISHING_LURE, original);
        }

        return original;
    }
}
