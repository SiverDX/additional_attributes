package de.cadentem.additional_attributes.mixin.irons_spellbooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import de.cadentem.additional_attributes.config.ServerConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.eldritch.AbstractEldritchSpell;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Innate spells unlock the usage of eldritch spells (if enabled through the config) */
@Mixin(value = AbstractEldritchSpell.class, remap = false)
public abstract class AbstractEldritchSpellMixin extends AbstractSpell {
    @SuppressWarnings("ConstantConditions") // attributes are present
    @ModifyReturnValue(method = "isLearned", at = @At("RETURN"))
    private boolean additional_attributes$innateUnlocksEldritch(boolean isLearned, @Local(argsOnly = true) @Nullable final Player player) {
        if (player == null) {
            return isLearned;
        }

        if (!isLearned && ServerConfig.INNATE_UNLOCKS_ELDRITCH.get()) {
            if (player.getAttribute(ISAttributes.getInnateSchool(getSchoolType().getId())).getValue() > 0) {
                return true;
            } else {
                return player.getAttribute(ISAttributes.getInnateSpell(getSpellResource())).getValue() > 0;
            }
        }

        return isLearned;
    }
}
