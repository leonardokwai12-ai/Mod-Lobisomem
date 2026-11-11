package com.lobisomem.mod;

import com.lobisomem.mod.entity.WerewolfEntity;
import com.lobisomem.mod.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

public class LobisomemMod implements ModInitializer {
    public static final String MOD_ID = "lobisomemmod";

    public static final EntityType<WerewolfEntity> WEREWOLF = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "werewolf"),
            EntityType.Builder.create(WerewolfEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.9f, 2.1f)
                    .build("werewolf")
    );

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        FabricDefaultAttributeRegistry.register(WEREWOLF, WerewolfEntity.createWerewolfAttributes());

        SpawnRestriction.register(WEREWOLF, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), SpawnGroup.MONSTER, WEREWOLF, 100, 1, 4);
    }
}