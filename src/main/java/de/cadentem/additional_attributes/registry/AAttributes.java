package de.cadentem.additional_attributes.registry;

import de.cadentem.additional_attributes.AA;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class AAttributes {
    public static final int MAX = 1024;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, AA.MODID);

    public static final DeferredHolder<Attribute, Attribute> FISHING_LURE = createAttribute("fishing_lure");
    public static final DeferredHolder<Attribute, Attribute> FISHING_LUCK = createAttribute("fishing_luck");
    public static final DeferredHolder<Attribute, Attribute> LOOTING = createAttribute("looting");
    public static final DeferredHolder<Attribute, Attribute> RESPIRATION = createAttribute("respiration");
    public static final DeferredHolder<Attribute, Attribute> HARVEST_BONUS = createAttribute("harvest");

    public static DeferredHolder<Attribute, Attribute> createAttribute(final String id) {
        return ATTRIBUTES.register(id, () -> new RangedAttribute("attribute." + AA.MODID + "." + id, 0, 0, MAX).setSyncable(true));
    }

    @SubscribeEvent
    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> event.add(EntityType.PLAYER, attribute));
    }

    @SuppressWarnings("ConstantConditions")
    public static int getIntValue(final LivingEntity entity, final Holder<Attribute> attribute, double base) {
        if (/* Can be null */ entity.getAttributes() == null || !entity.getAttributes().hasAttribute(attribute)) {
            return (int) base;
        }

        double value = getAttributeValue(entity, attribute, base);
        int clippedValue = (int) value;

        if (entity.getRandom().nextFloat() < value - clippedValue) {
            value++;
        }

        return (int) value;
    }

    public static double getAttributeValue(final LivingEntity entity, final Holder<Attribute> attribute, double base) {
        if (attribute == null) {
            return 0;
        }

        AttributeInstance instance = entity.getAttribute(attribute);

        if (instance == null) {
            return 0;
        }

        if (instance.getBaseValue() != base) {
            instance.setBaseValue(base);
        }

        return instance.getValue();
    }
}
