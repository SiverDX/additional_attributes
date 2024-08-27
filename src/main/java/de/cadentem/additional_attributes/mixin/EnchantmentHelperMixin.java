package de.cadentem.additional_attributes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.cadentem.additional_attributes.registry.AAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    // TODO :: processEquipmentDropChance -> simulate and / or modify looting

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

    @ModifyReturnValue(method = "getEnchantmentLevel", at = @At("RETURN"))
    private static int additional_attributes$modifyLootingLevel(int original, @Local(argsOnly = true) final Holder<Enchantment> enchantment, @Local(argsOnly = true) final LivingEntity entity) {
        if (enchantment.is(Enchantments.LOOTING)) {
            return AAttributes.getIntValue(entity, AAttributes.LOOTING, original);
        }

        return original;
    }
}
