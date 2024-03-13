package de.cadentem.additional_attributes.mixin.aquaculture;

import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import de.cadentem.additional_attributes.mixin.FishingHookAccess;
import de.cadentem.additional_attributes.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AquaFishingBobberEntity.class)
@Debug(export = true)
public abstract class AquaFishingBobberEntityMixin extends FishingHook {
    public AquaFishingBobberEntityMixin(final Player player, final Level level, int luck, int lureSpeed) {
        super(player, level, luck, lureSpeed);
    }

    @Inject(method = "catchingFish", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, ordinal = 11, shift = At.Shift.AFTER))
    private void additional_attributes$fixLure(final BlockPos position, final CallbackInfo callback) {
        ((FishingHookAccess) this).setTimeUntilLured(Utils.getTicksCaughtDelay(this));
    }
}
