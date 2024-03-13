package de.cadentem.additional_attributes.mixin.aquaculture;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import de.cadentem.additional_attributes.registry.AAttributes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AquaFishingRodItem.class)
public abstract class AquaFishingRodItemMixin {
    @Inject(method = "use", at = @At("HEAD"))
    private void additional_attributes$storePlayer(final Level level, final Player player, final InteractionHand hand, final CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callback, @Share("player") final LocalRef<Player> storedPlayer) {
        storedPlayer.set(player);
    }

    @ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lcom/teammetallurgy/aquaculture/entity/AquaFishingBobberEntity;<init>(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;IILcom/teammetallurgy/aquaculture/api/fishing/Hook;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V"), index = 3)
    private int additional_attributes$amplifyLure(int original, @Share("player") final LocalRef<Player> storedPlayer) {
        return AAttributes.getIntValue(storedPlayer.get(), AAttributes.FISHING_LURE.get(), original);
    }

    @ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lcom/teammetallurgy/aquaculture/entity/AquaFishingBobberEntity;<init>(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;IILcom/teammetallurgy/aquaculture/api/fishing/Hook;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V"), index = 2)
    private int additional_attributes$amplifyLuck(int original, @Share("player") final LocalRef<Player> storedPlayer) {
        return AAttributes.getIntValue(storedPlayer.get(), AAttributes.FISHING_LUCK.get(), original);
    }
}
