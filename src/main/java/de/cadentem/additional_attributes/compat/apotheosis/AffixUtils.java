package de.cadentem.additional_attributes.compat.apotheosis;

import com.mojang.datafixers.util.Pair;
import de.cadentem.additional_attributes.config.ServerConfig;
import de.cadentem.additional_attributes.datagen.AAItemTags;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootController;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityClamp;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AffixUtils {
    public static void affixItem(final ItemStack stack, final Player player) {
        if (stack.is(AAItemTags.APOTH_CRAFTING_BLACKLIST)) {
            return;
        }

        double crafting = player.getAttributeValue(ApothAttributes.APOTHIC_CRAFTING.get());

        if (crafting == 0 || LootCategory.forItem(stack) == LootCategory.NONE) {
            return;
        }

        if (crafting < 1 && player.getRandom().nextDouble() > crafting) {
            return;
        }

        List<DynamicHolder<LootRarity>> rarities = RarityRegistry.INSTANCE.getOrderedRarities();

        if (rarities.isEmpty()) {
            return;
        }

        int rounded = (int) Math.min(ServerConfig.MAX_RARITY.get(), Math.floor(crafting));

        if (crafting >= 1) {
            // Since 0 to 1 is between nothing and common (0) we need to subtract 1
            // So that 1 to 2 is between common (0) and uncommon (1)
            // In turn the rarity has no chance to increase if crafting is below 1
            rounded--;
        }

        DynamicHolder<LootRarity> min = rounded < rarities.size() ? rarities.get(rounded) : RarityRegistry.getMaxRarity();
        DynamicHolder<LootRarity> max = min;

        if (shouldIncreaseRarity(player.getRandom(), crafting)) {
            max = RarityRegistry.next(max);
        }

        int shift = 0;

        while (shift < ServerConfig.SHIFT_MIN_RARITY.get()) {
            min = RarityRegistry.prev(min);
            shift++;
        }

        Pair<DynamicHolder<LootRarity>, DynamicHolder<LootRarity>> clamp = RarityDefinition.clamp(stack, min, max);
        LootRarity rarity = LootRarity.random(player.getRandom(), player.getLuck(), new RarityClamp.Simple(clamp.getFirst(), clamp.getSecond()));
        LootController.createLootItem(stack, rarity, player.getRandom());
    }

    private static boolean shouldIncreaseRarity(final RandomSource random, double crafting) {
        int clamped = (int) crafting;
        double remainder = crafting - clamped;

        if (crafting < 1 || remainder == 0 || crafting >= RarityRegistry.INSTANCE.getOrderedRarities().size()) {
            return false;
        }

        return random.nextDouble() < remainder;
    }
}
