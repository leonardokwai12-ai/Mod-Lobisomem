package com.lobisomem.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import com.lobisomem.mod.entity.WerewolfEntity;

public class LobisomemModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(LobisomemMod.WEREWOLF, (context) ->
            new MobEntityRenderer<WerewolfEntity, BipedEntityModel<WerewolfEntity>>(
                context,
                new BipedEntityModel<>(context.getPart(EntityModelLayers.ZOMBIE)),
                0.7f
            ) {
                @Override
                public Identifier getTexture(WerewolfEntity entity) {
                    return Identifier.of("minecraft", "textures/entity/wolf/wolf_black.png");
                }
            }
        );
    }
}