package com.fuyuvulpes.yoamod.datagen.generators;

import com.fuyuvulpes.yoamod.registries.BlocksModReg;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import static com.fuyuvulpes.yoamod.YOAMod.MODID;

public class BlockStateGen extends BlockStateProvider {
    public BlockStateGen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlocksModReg.CREAKSTONE);
        blockWithItem(BlocksModReg.CREAKSTONE_FRACTURE);



        blockWithItem(BlocksModReg.SILVER_ORE);
        blockWithItem(BlocksModReg.DEEPSLATE_SILVER_ORE);
        blockWithItem(BlocksModReg.TITANIUM_ORE);
        blockWithItem(BlocksModReg.DEEPSLATE_TITANIUM_ORE);
        blockWithItem(BlocksModReg.BISMUTH_ORE);
        blockWithItem(BlocksModReg.DEEPSLATE_BISMUTH_ORE);
        blockWithItem(BlocksModReg.WITHERITE_ORE);
        blockWithItem(BlocksModReg.IOLITE_ORE);
        blockWithItem(BlocksModReg.ALEXANDRITE_ORE);
        blockWithItem(BlocksModReg.ADAMANTITE_ORE);
    }


    private void blockWithItem(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}
