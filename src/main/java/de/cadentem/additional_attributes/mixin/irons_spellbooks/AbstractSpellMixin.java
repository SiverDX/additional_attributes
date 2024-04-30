package de.cadentem.additional_attributes.mixin.irons_spellbooks;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractSpell.class, remap = false)
public abstract class AbstractSpellMixin {
    @Unique private static final CastResult additional_attributes$NO_SPELL_LEVEL_FAILURE = new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.additional_attributes.cast_error_no_spell_level"));

    @Inject(method = "canBeCastedBy", at = @At("HEAD"), cancellable = true)
    private void additional_attributes$cancelForNoSpellLevel(int spellLevel, final CastSource castSource, final MagicData magicData, final Player player, final CallbackInfoReturnable<CastResult> callback) {
        if (spellLevel == 0) {
            callback.setReturnValue(additional_attributes$NO_SPELL_LEVEL_FAILURE);
        }
    }
}
