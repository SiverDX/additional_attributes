package de.cadentem.additional_attributes.compat.apotheosis;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cadentem.additional_attributes.AA;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RarityDefinition extends SimpleJsonResourceReloadListener {
    private static final Set<Clamp> ENTRIES = new HashSet<>();

    private final RegistryAccess access;

    public RarityDefinition(final RegistryAccess access) {
        super(new Gson(), "additional_attributes/rarity_definitions");
        this.access = access;
    }

    public static Pair<DynamicHolder<LootRarity>, DynamicHolder<LootRarity>> clamp(final ItemStack stack, DynamicHolder<LootRarity> min, DynamicHolder<LootRarity> max) {
        for (Clamp clamp : ENTRIES) {
            if (clamp.items().contains(stack.getItemHolder())) {
                DynamicHolder<LootRarity> clampedMin = clamp.minRarity().orElse(null);
                DynamicHolder<LootRarity> clampedMax = clamp.maxRarity().orElse(null);

                if (clampedMax != null && clampedMax.get().ordinal() < max.get().ordinal()) {
                    max = clampedMax;
                }

                if (clampedMin != null && clampedMin.get().ordinal() < max.get().ordinal()) {
                    min = clampedMin;
                } else if (min.get().ordinal() > max.get().ordinal()) {
                    min = max;
                }

                return Pair.of(min, max);
            }
        }

        return Pair.of(min, max);
    }

    public static void register(final AddReloadListenerEvent event) {
        event.addListener(new RarityDefinition(event.getRegistryAccess()));
    }

    @Override
    protected void apply(@NotNull final Map<ResourceLocation, JsonElement> data, @NotNull final ResourceManager manager, @NotNull final ProfilerFiller profiler) {
        RegistryOps<JsonElement> serializer = RegistryOps.create(JsonOps.INSTANCE, access);
        data.values().forEach(element -> Clamp.CODEC.decode(serializer, element).resultOrPartial(AA.LOG::error).ifPresent(result -> {
            Clamp clamp = result.getFirst();

            if (clamp.minRarity().map(minRarity -> clamp.maxRarity().map(maxRarity -> minRarity.get().ordinal() > maxRarity.get().ordinal()).orElse(false)).orElse(false)) {
                AA.LOG.error("Invalid rarity definition for {}: min rarity {} is greater than max rarity {}", clamp.items(), clamp.minRarity().get(), clamp.maxRarity().get());
            } else {
                ENTRIES.add(clamp);
            }
        }));
    }

    private record Clamp(HolderSet<Item> items, Optional<DynamicHolder<LootRarity>> minRarity, Optional<DynamicHolder<LootRarity>> maxRarity) {
        public static final Codec<Clamp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(Clamp::items),
                RarityRegistry.INSTANCE.holderCodec().optionalFieldOf("min_rarity").forGetter(Clamp::minRarity),
                RarityRegistry.INSTANCE.holderCodec().optionalFieldOf("max_rarity").forGetter(Clamp::minRarity)
        ).apply(instance, Clamp::new));
    }
}
