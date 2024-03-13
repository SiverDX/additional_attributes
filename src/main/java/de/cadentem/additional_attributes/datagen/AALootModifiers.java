package de.cadentem.additional_attributes.datagen;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.loot.HarvestLootAmplifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class AALootModifiers extends GlobalLootModifierProvider {
    public AALootModifiers(final PackOutput output) {
        super(output, AA.MODID);
    }

    @Override
    protected void start() {
        add(HarvestLootAmplifier.ID, new HarvestLootAmplifier(new LootItemCondition[]{}));
    }
}
