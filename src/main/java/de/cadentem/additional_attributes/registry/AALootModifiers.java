package de.cadentem.additional_attributes.registry;

import com.mojang.serialization.MapCodec;
import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.loot.HarvestLootAmplifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AALootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.key(), AA.MODID);

    static {
        LOOT_MODIFIERS.register(HarvestLootAmplifier.ID, () -> HarvestLootAmplifier.CODEC);
    }
}
