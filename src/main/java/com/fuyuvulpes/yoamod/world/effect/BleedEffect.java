package com.fuyuvulpes.yoamod.world.effect;

import com.fuyuvulpes.yoamod.core.registries.YoaKeys;
import com.fuyuvulpes.yoamod.core.registries.YoaParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BleedEffect extends MobEffect {
    public BleedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        super.applyEffectTick(entity, pAmplifier);
        entity.hurt(entity.damageSources().source(YoaKeys.BLEEDING), 1.3F * (pAmplifier + 1));

        Level level = entity.level();
        for (int i = 5 + (pAmplifier * 3); i > 0; i--){
            if (!level.isClientSide){
                ServerLevel serverLevel = (ServerLevel) level;
                serverLevel.sendParticles(YoaParticles.BLEEDING.get(),entity.getX(),entity.getY() + (entity.getBbHeight() / 2) ,entity.getZ(),entity.getRandom().nextIntBetweenInclusive(3,6),
                        entity.getRandomX(0.5),entity.getRandomY(),entity.getRandomZ(0.5),0.01F);
            }
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        int i = 32 >> pAmplifier;
        if (i > 0) {
            return pDuration % i == 0;
        } else {
            return true;
        }
    }



}
