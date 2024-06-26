package com.fuyuvulpes.yoamod.game.client.entities.renderers;

import com.fuyuvulpes.yoamod.game.client.entities.model.OwlModel;
import com.fuyuvulpes.yoamod.world.entity.OwlEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.fuyuvulpes.yoamod.YOAMod.MODID;

public class OwlRenderer extends MobRenderer<OwlEntity, OwlModel<OwlEntity>> {
    private static final ResourceLocation OWL_TEXTURE = new ResourceLocation(MODID,"textures/entity/owl.png");

    public OwlRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new OwlModel<>(pContext.bakeLayer(OwlModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(OwlEntity owl) {
        return OWL_TEXTURE;
    }
}
