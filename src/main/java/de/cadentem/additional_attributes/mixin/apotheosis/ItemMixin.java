package de.cadentem.additional_attributes.mixin.apotheosis;

import de.cadentem.additional_attributes.compat.Compat;
import de.cadentem.additional_attributes.compat.apotheosis.AffixUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "onCraftedBy", at = @At("HEAD"))
    private void additional_attributes$affixItem(final ItemStack stack, final Level level, final Player player, final CallbackInfo callback) {
        if (Compat.isModLoaded(Compat.APOTHEOSIS)) {
            AffixUtils.affixItem(stack, player);
        }
    }
}
