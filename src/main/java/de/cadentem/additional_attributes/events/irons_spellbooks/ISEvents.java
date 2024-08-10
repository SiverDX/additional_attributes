package de.cadentem.additional_attributes.events.irons_spellbooks;

import de.cadentem.additional_attributes.AA;
import de.cadentem.additional_attributes.compat.irons_spellbooks.ISAttributes;
import de.cadentem.additional_attributes.utils.SpellUtils;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class ISEvents {
    private static final int INDEX_START = 100;

    public static void modifyLevel(final ModifySpellLevelEvent event) {
        event.setLevel(SpellUtils.calculateSpellLevel(event.getEntity(), event.getSpell(), event.getLevel()));
    }

    public static void modifySpellSelection(final SpellSelectionManager.SpellSelectionEvent event) {
        if (ISAttributes.areInnateListsMissing()) {
            return;
        }

        int indexOffset = 0;

        for (Attribute attribute : ISAttributes.INNATE_SCHOOLS) {
            AttributeInstance instance = event.getEntity().getAttribute(attribute);

            if (instance != null) {
                int level = (int) instance.getValue();

                if (level > 0) {
                    String location = attribute.getDescriptionId().replace(ISAttributes.DESCRIPTION_PREFIX + ISAttributes.INNATE_SCHOOL_PREFIX, "").replaceFirst(ISAttributes.SEPARATOR, ":");
                    SchoolType school = SchoolRegistry.REGISTRY.get().getValue(ResourceLocation.tryParse(location));

                    for (AbstractSpell spell : SpellRegistry.REGISTRY.get().getValues()) {
                        if (spell.getSchoolType() == school) {
                            event.addSelectionOption(new SpellData(spell, level), ISAttributes.INNATE_SCHOOL_PREFIX + location, INDEX_START + indexOffset);
                            indexOffset++;
                        }
                    }
                }
            }
        }

        // TODO :: spells + combine levels with school
    }

    public static void test(ItemAttributeModifierEvent event) { // FIXME :: remove
        if (event.getSlotType() == EquipmentSlot.HEAD && event.getItemStack().getItem() == Items.IRON_HELMET) {
            event.addModifier(ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(AA.MODID + ":" + ISAttributes.INNATE_SCHOOL_PREFIX + "irons_spellbooks/fire")), new AttributeModifier(UUID.fromString("6d617f15-f857-4adb-a621-6db396556a7e"), "test", 3, AttributeModifier.Operation.ADDITION));
        }
    }

    public static void reload(final TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayer) {
            Boolean shouldReload = ISAttributes.SHOULD_RELOAD.get(serverPlayer);

            if (shouldReload != null && shouldReload) {
                ISAttributes.SHOULD_RELOAD.remove(serverPlayer);
                ClientMagicData.updateSpellSelectionManager(serverPlayer); // TODO :: only set client-side? (i.e. mixin also triggers reload on attribute packet received?)
//                ClientMagicData.updateSpellSelectionManager();
            }
        }
    }

    public static void clear(final EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ISAttributes.SHOULD_RELOAD.remove(serverPlayer);
        }
    }

    public static void initListsServer(final ServerStartedEvent ignored) {
        ISAttributes.initInnateLists();
    }

    public static void initListsClient(final EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player && event.getLevel().isClientSide() && ISAttributes.areInnateListsMissing()) {
            ISAttributes.initInnateLists();
        }
    }
}
