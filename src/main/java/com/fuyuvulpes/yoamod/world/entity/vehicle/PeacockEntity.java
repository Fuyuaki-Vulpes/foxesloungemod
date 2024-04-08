package com.fuyuvulpes.yoamod.world.entity.vehicle;

import com.fuyuvulpes.yoamod.world.entity.FennecFoxEntity;
import com.fuyuvulpes.yoamod.world.entity.OwlEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class PeacockEntity extends Animal implements VariantHolder<PeacockEntity.Type> {
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(PeacockEntity.class, EntityDataSerializers.INT);

    public PeacockEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new PanicGoal(this, 0.6));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.0f, Ingredient.of(Items.WHEAT_SEEDS), false));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0, 3.0F, 7.0F));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0F));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return ShoulderRidingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,12)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.FOLLOW_RANGE, 32f);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    public Type getVariant() {
        return Type.byId(this.entityData.get(DATA_TYPE_ID));
    }

    public void setVariant(Type pVariant) {
        this.entityData.set(DATA_TYPE_ID, pVariant.getId());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 0);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Type", this.getVariant().id);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(PeacockEntity.Type.byId(pCompound.getInt("Type")));
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 1.0F;
    }

    public static boolean canSpawn(EntityType<PeacockEntity> pPeacock, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return Animal.checkAnimalSpawnRules(pPeacock,pLevel,pSpawnType,pPos,pRandom);
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.setVariant(Util.getRandom(PeacockEntity.Type.values(), pLevel.getRandom()));
        if (pSpawnData == null) {
            pSpawnData = new AgeableMob.AgeableMobGroupData(false);
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }


    public static enum Type implements StringRepresentable {
        PEACOCK(0, "peacock"),
        PEAHEN(1, "peahen");

        public static final EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        private final int id;
        private final String name;

        private Type(int pId, String pName) {
            this.id = pId;
            this.name = pName;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }

        public static Type byName(String pName) {
            return CODEC.byName(pName, PEACOCK);
        }

        public static Type byId(int pIndex) {
            return BY_ID.apply(pIndex);
        }
    }
}
