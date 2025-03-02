package de.cadentem.additional_attributes.compat;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compat {
    public static final String IRONS_SPELLBOOKS = "irons_spellbooks";
    public static final String APOTHEOSIS = "apotheosis";

    private static final Map<String, List<String>> ALIAS = Map.of();
    private static final Map<String, Boolean> MODS = new HashMap<>();

    public static boolean isModLoaded(final String mod) {
        return MODS.computeIfAbsent(mod, key -> {
            if (check(key)) {
                return true;
            }

            for (String alias : ALIAS.getOrDefault(key, List.of())) {
                if (check(alias)) {
                    return true;
                }
            }

            return false;
        });
    }

    private static boolean check(final String modid) {
        ModList modList = ModList.get();

        if (modList != null && modList.isLoaded(modid)) {
            return true;
        }

        return LoadingModList.get().getModFileById(modid) != null;
    }
}
