package com.lobisomem.mod.item;

import com.lobisomem.mod.LobisomemMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item STONE_BULLET = registerItem("stone_bullet", new Item(new Item.Settings()));
    public static final Item RIFLE = registerItem("rifle", new RifleItem(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(LobisomemMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(RIFLE);
            entries.add(STONE_BULLET);
        });
    }
}