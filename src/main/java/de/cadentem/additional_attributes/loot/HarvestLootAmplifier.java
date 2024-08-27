package de.cadentem.additional_attributes.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cadentem.additional_attributes.registry.AAttributes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class HarvestLootAmplifier extends LootModifier {
    public static final String ID = "harvest_loot_amplifier";
    public static final MapCodec<HarvestLootAmplifier> CODEC = RecordCodecBuilder.mapCodec(instance -> LootModifier.codecStart(instance).apply(instance, HarvestLootAmplifier::new));

    public HarvestLootAmplifier(final LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull final ObjectArrayList<ItemStack> loot, final LootContext context) {
        if (!context.hasParam(LootContextParams.BLOCK_STATE) || !context.hasParam(LootContextParams.THIS_ENTITY)) {
            return loot;
        }

        BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
        Entity entity = context.getParam(LootContextParams.THIS_ENTITY);

        if (entity instanceof ServerPlayer serverPlayer && state.is(BlockTags.CROPS)) {
            loot.stream().filter(item -> item.is(Tags.Items.CROPS)).forEach(item -> item.setCount(AAttributes.getIntValue(serverPlayer, AAttributes.HARVEST_BONUS, item.getCount())));
        }

        return loot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}