package com.fuyuvulpes.yoamod.game.client.entities.renderers;

import com.fuyuvulpes.yoamod.game.client.entities.model.ChakramModel;
import com.fuyuvulpes.yoamod.world.entity.projectile.ChakramEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ChakramRenderer<T extends ChakramEntity> extends EntityRenderer<T> {

    private final ChakramModel<T> model;


    public ChakramRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ChakramModel<>(pContext.bakeLayer(ChakramModel.LAYER_LOCATION));

    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(pEntity.getTexture()));
        pPoseStack.translate(0,-1.5,0);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return pEntity.getTexture();
    }
}