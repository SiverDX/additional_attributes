package de.cadentem.additional_attributes.mixin.irons_spellbooks;

import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import de.cadentem.additional_attributes.compat.irons_spellbooks.InnateAttribute;
import de.cadentem.additional_attributes.datagen.AALanguageProvider;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Attribute.class)
public abstract class AttributeMixin implements InnateAttribute {
    @Unique private boolean additional_attributes$isInnateAttribute;
    @Shadow @Final private String descriptionId;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void additional_attributes$setInnateFlag(final CallbackInfo callback) {
        if (descriptionId.startsWith(AALanguageProvider.PREFIX + ISAttributes.INNATE_SCHOOL_PREFIX) || descriptionId.startsWith(AALanguageProvider.PREFIX + ISAttributes.INNATE_SPELL_PREFIX)) {
            additional_attributes$isInnateAttribute = true;
        }
    }

    @Override
    public boolean additional_attributes$isInnateAttribute() {
        return additional_attributes$isInnateAttribute;
    }
}
