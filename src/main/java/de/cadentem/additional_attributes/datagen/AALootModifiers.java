package de.cadentem.additional_attributes.datagen;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.loot.HarvestLootAmplifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class AALootModifiers extends GlobalLootModifierProvider {
    public AALootModifiers(final PackOutput output, final CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, AA.MODID);
    }

    @Override
    protected void start() {
        add(HarvestLootAmplifier.ID, new HarvestLootAmplifier(new LootItemCondition[]{}));
    }
}
