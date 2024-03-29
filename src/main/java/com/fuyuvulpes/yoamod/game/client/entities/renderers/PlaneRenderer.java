package com.fuyuvulpes.yoamod.game.client.entities.renderers;

import com.fuyuvulpes.yoamod.game.client.entities.model.BrawlerModel;
import com.fuyuvulpes.yoamod.game.client.entities.model.PlaneModel;
import com.fuyuvulpes.yoamod.world.entity.BrawlerEntity;
import com.fuyuvulpes.yoamod.world.entity.vehicle.PlaneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import static com.fuyuvulpes.yoamod.YOAMod.MODID;

public class PlaneRenderer extends EntityRenderer<PlaneEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID,"textures/entity/vehicles/plane.png");
    private static final ResourceLocation FLIGHT_TEXTURE = new ResourceLocation(MODID,"textures/entity/vehicles/plane_flight.png");
    public final PlaneModel<PlaneEntity> model;

    public PlaneRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new PlaneModel<>(pContext.bakeLayer(PlaneModel.LAYER_LOCATION));

    }

    @Override
    public ResourceLocation getTextureLocation(PlaneEntity pEntity) {
        if (pEntity.getDeltaMovement().length() > 10){
            return FLIGHT_TEXTURE;
        }
        return TEXTURE;
    }

    @Override
    public void render(PlaneEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 1.5F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float)pEntity.getHurtTime() - pPartialTick;
        float f1 = pEntity.getDamage() - pPartialTick;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)pEntity.getHurtDir()));
        }




        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.setupAnim(pEntity, pPartialTick, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);


        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

    }
}
