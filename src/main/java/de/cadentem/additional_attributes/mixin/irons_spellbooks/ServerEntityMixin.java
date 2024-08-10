package de.cadentem.additional_attributes.mixin.irons_spellbooks;

import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
    @Shadow @Final private Entity entity;

    @Inject(method = "sendDirtyEntityData", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerEntity;broadcastAndSend(Lnet/minecraft/network/protocol/Packet;)V"))
    private void additional_attributes$updateSpellSelection(final CallbackInfo callback) {
        if (entity instanceof Player player) {
            ISAttributes.SHOULD_RELOAD.put(player, true);
        }
    }
}
