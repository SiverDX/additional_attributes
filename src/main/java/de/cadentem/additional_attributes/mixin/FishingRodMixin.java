package de.cadentem.additional_attributes.mixin;

import de.cadentem.additional_attributes.registry.AAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishingRodItem.class)
public abstract class FishingRodMixin {
    @ModifyVariable(method = "use", at = @At(value = "STORE"), name = "k")
    private int additional_attributes$amplifyLure(int original, /* Method arguments: */ final Level level, final Player player) {
        return AAttributes.getIntValue(player, AAttributes.FISHING_LURE.get(), original);
    }

    @ModifyVariable(method = "use", at = @At(value = "STORE"), name = "j")
    private int additional_attributes$amplifyLuck(int original, /* Method arguments: */ final Level level, final Player player) {
        return AAttributes.getIntValue(player, AAttributes.FISHING_LUCK.get(), original);
    }
}
