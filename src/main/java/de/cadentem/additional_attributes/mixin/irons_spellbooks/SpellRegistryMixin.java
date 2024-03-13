package de.cadentem.additional_attributes.mixin.irons_spellbooks;

import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SpellRegistry.class, remap = false)
public abstract class SpellRegistryMixin {
    @Inject(method = "registerSpell", at = @At("RETURN"))
    private static void additional_attributes$registerSpellAttribute(final AbstractSpell spell, final CallbackInfoReturnable<RegistryObject<AbstractSpell>> callback) {
        ISAttributes.createAttribute("spell_type_" + spell.getSpellName());
    }
}
