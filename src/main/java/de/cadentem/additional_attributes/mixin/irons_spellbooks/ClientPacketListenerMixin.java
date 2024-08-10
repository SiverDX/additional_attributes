package de.cadentem.additional_attributes.mixin.irons_spellbooks;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Update the spell selection if relevant attributes changed */
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleUpdateAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V", shift = At.Shift.BEFORE))
    private void additional_attributes$flagSync(final ClientboundUpdateAttributesPacket packet, final CallbackInfo callback, @Local final AttributeInstance instance, @Share("shouldSync") final LocalRef<Boolean> shouldSync) {
        if (ISAttributes.areInnateListsMissing() || shouldSync.get() != null) {
            return;
        }

        Attribute attribute = instance.getAttribute();

        if (ISAttributes.INNATE_SCHOOLS.contains(attribute) || ISAttributes.INNATE_SPELLS.contains(attribute)) {
            shouldSync.set(true);
        }
    }

    @Inject(method = "handleUpdateAttributes", at = @At(value = "RETURN"))
    private void additional_attributes$triggerSync(final ClientboundUpdateAttributesPacket packet, final CallbackInfo callback, @Share("shouldSync") final LocalRef<Boolean> shouldSync) {
        if (shouldSync.get() != null) {
            ClientMagicData.updateSpellSelectionManager();
        }
    }
}
