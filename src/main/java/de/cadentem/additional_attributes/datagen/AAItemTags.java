package de.cadentem.additional_attributes.datagen;

import de.cadentem.additional_attributes.AA;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AAItemTags extends ItemTagsProvider {
    public static final TagKey<Item> APOTH_CRAFTING_BLACKLIST = TagKey.create(Registries.ITEM, new ResourceLocation(AA.MODID, "apothic_crafting_blacklist"));

    public AAItemTags(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookup, final CompletableFuture<TagLookup<Block>> blockTags, @Nullable final ExistingFileHelper helper) {
        super(output, lookup, blockTags, AA.MODID, helper);
    }

    @Override
    protected void addTags(@NotNull final HolderLookup.Provider pProvider) {
        tag(APOTH_CRAFTING_BLACKLIST);
    }
}
