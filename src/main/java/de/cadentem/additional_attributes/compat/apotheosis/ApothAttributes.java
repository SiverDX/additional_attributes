package de.cadentem.additional_attributes.compat.apotheosis;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.datagen.AALanguageProvider;
import dev.shadowsoffire.attributeslib.impl.PercentBasedAttribute;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ApothAttributes {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, AA.MODID);
    public static RegistryObject<Attribute> APOTHIC_CRAFTING = REGISTRY.register("apothic_crafting", () -> new PercentBasedAttribute(AALanguageProvider.PREFIX + "apothic_crafting", 0, 0, 10).setSyncable(true));

    public static void setAttributes(final EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, APOTHIC_CRAFTING.get());
    }
}
