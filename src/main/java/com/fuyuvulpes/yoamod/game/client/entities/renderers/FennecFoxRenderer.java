package com.fuyuvulpes.yoamod.game.client.entities.renderers;

import com.fuyuvulpes.yoamod.game.client.entities.model.FennecFoxModel;
import com.fuyuvulpes.yoamod.world.entity.FennecFoxEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.fuyuvulpes.yoamod.YOAMod.MODID;

public class FennecFoxRenderer extends MobRenderer<FennecFoxEntity, FennecFoxModel<FennecFoxEntity>> {
    private static final ResourceLocation FENNEC_FOX_TEXTURE = new ResourceLocation(MODID,"textures/entity/fennec_fox/fennec_fox.png");
    private static final ResourceLocation FENNEC_FOX_SLEEP_TEXTURE = new ResourceLocation(MODID,"textures/entity/fennec_fox/fennec_fox_sleeping.png");

    public FennecFoxRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new FennecFoxModel<>(pContext.bakeLayer(FennecFoxModel.LAYER_LOCATION)), 0.2F);
    }
    protected void setupRotations(FennecFoxEntity pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        if (pEntityLiving.isPouncing() || pEntityLiving.isFaceplanted()) {
            float f = -Mth.lerp(pPartialTicks, pEntityLiving.xRotO, pEntityLiving.getXRot());
            pPoseStack.mulPose(Axis.XP.rotationDegrees(f));
        }

    }
    @Override
    public void render(FennecFoxEntity pFox, float pEntityYaw, float pParticalTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight){
        if(pFox.isBaby()){
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);

        }
        super.render(pFox,pEntityYaw,pParticalTicks,pMatrixStack,pBuffer,pPackedLight);
    }

    public ResourceLocation getTextureLocation(FennecFoxEntity pEntity) {
            return pEntity.isSleeping() ? FENNEC_FOX_SLEEP_TEXTURE : FENNEC_FOX_TEXTURE;
        }
    }
