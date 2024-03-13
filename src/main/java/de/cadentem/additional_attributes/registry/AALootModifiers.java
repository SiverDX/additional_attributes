package de.cadentem.additional_attributes.registry;

import com.mojang.serialization.Codec;
import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.loot.HarvestLootAmplifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AALootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AA.MODID);

    public static final RegistryObject<Codec<HarvestLootAmplifier>> HARVEST_LOOT_AMPLIFIER = LOOT_MODIFIERS.register(HarvestLootAmplifier.ID, () -> HarvestLootAmplifier.CODEC);
}
