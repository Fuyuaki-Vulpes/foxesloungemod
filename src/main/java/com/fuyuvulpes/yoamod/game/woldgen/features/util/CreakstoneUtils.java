package com.fuyuvulpes.yoamod.game.woldgen.features.util;

import com.fuyuvulpes.yoamod.core.registries.YoaBlocks;
import com.fuyuvulpes.yoamod.world.block.PointedCreakstone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;

import java.util.function.Consumer;

public class CreakstoneUtils {
        protected static double getDripstoneHeight(double pRadius, double pMaxRadius, double pScale, double pMinRadius) {
    if (pRadius < pMinRadius) {
        pRadius = pMinRadius;
    }

    double d0 = 0.384;
    double d1 = pRadius / pMaxRadius * 0.384;
    double d2 = 0.75 * Math.pow(d1, 1.3333333333333333);
    double d3 = Math.pow(d1, 0.6666666666666666);
    double d4 = 0.3333333333333333 * Math.log(d1);
    double d5 = pScale * (d2 - d3 - d4);
    d5 = Math.max(d5, 0.0);
    return d5 / 0.384 * pMaxRadius;
}

        protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel pLevel, BlockPos pPos, int pRadius) {
            if (isEmptyOrWaterOrLava(pLevel, pPos)) {
                return false;
            } else {
                float f = 6.0F;
                float f1 = 6.0F / (float)pRadius;

                for(float f2 = 0.0F; f2 < (float) (Math.PI * 2); f2 += f1) {
                    int i = (int)(Mth.cos(f2) * (float)pRadius);
                    int j = (int)(Mth.sin(f2) * (float)pRadius);
                    if (isEmptyOrWaterOrLava(pLevel, pPos.offset(i, 0, j))) {
                        return false;
                    }
                }

                return true;
            }
        }

        public static boolean isEmptyOrWater(LevelAccessor pLevel, BlockPos pPos) {
            return pLevel.isStateAtPosition(pPos, DripstoneUtils::isEmptyOrWater);
        }

        protected static boolean isEmptyOrWaterOrLava(LevelAccessor pLevel, BlockPos pPos) {
            return pLevel.isStateAtPosition(pPos, DripstoneUtils::isEmptyOrWaterOrLava);
        }

        protected static void buildBaseToTipColumn(Direction pDirection, int pHeight, boolean pMergeTip, Consumer<BlockState> pBlockSetter) {
            if (pHeight >= 3) {
                pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.BASE));

                for(int i = 0; i < pHeight - 3; ++i) {
                    pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.MIDDLE));
                }
            }

            if (pHeight >= 2) {
                pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.FRUSTUM));
            }

            if (pHeight >= 1) {
                pBlockSetter.accept(createPointedDripstone(pDirection, pMergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
            }
        }

        public static void growPointedDripstone(LevelAccessor pLevel, BlockPos pPos, Direction pDirection, int pHeight, boolean pMergeTip) {
            if (isDripstoneBase(pLevel.getBlockState(pPos.relative(pDirection.getOpposite())))) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();
                buildBaseToTipColumn(pDirection, pHeight, pMergeTip, p_313662_ -> {
                    if (p_313662_.is(YoaBlocks.POINTED_CREAKSTONE.get())) {
                        p_313662_ = p_313662_.setValue(PointedCreakstone.WATERLOGGED, pLevel.isWaterAt(blockpos$mutableblockpos));
                    }

                    pLevel.setBlock(blockpos$mutableblockpos, p_313662_, 2);
                    blockpos$mutableblockpos.move(pDirection);
                });
            }
        }

        public static boolean placeDripstoneBlockIfPossible(LevelAccessor pLevel, BlockPos pPos) {
            BlockState blockstate = pLevel.getBlockState(pPos);
            if (blockstate.is(BlockTags.DRIPSTONE_REPLACEABLE)) {
                pLevel.setBlock(pPos, YoaBlocks.CREAKSTONE.get().defaultBlockState(), 2);
                return true;
            } else {
                return false;
            }
        }

        private static BlockState createPointedDripstone(Direction pDirection, DripstoneThickness pDripstoneThickness) {
            return YoaBlocks.POINTED_CREAKSTONE.get()
                    .defaultBlockState()
                    .setValue(PointedCreakstone.TIP_DIRECTION, pDirection)
                    .setValue(PointedCreakstone.THICKNESS, pDripstoneThickness);
        }

        public static boolean isDripstoneBaseOrLava(BlockState pState) {
            return isDripstoneBase(pState) || pState.is(Blocks.LAVA);
        }

        public static boolean isDripstoneBase(BlockState pState) {
            return pState.is(YoaBlocks.POINTED_CREAKSTONE.get()) || pState.is(BlockTags.DRIPSTONE_REPLACEABLE);
        }

        public static boolean isEmptyOrWater(BlockState p_159665_) {
            return p_159665_.isAir() || p_159665_.is(Blocks.WATER);
        }

        public static boolean isNeitherEmptyNorWater(BlockState pState) {
            return !pState.isAir() && !pState.is(Blocks.WATER);
        }

        public static boolean isEmptyOrWaterOrLava(BlockState p_159667_) {
            return p_159667_.isAir() || p_159667_.is(Blocks.WATER) || p_159667_.is(Blocks.LAVA);
        }
}

