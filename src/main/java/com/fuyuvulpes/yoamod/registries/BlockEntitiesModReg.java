package com.fuyuvulpes.yoamod.registries;

import com.fuyuvulpes.yoamod.custom.entity.block.ForgingTableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.fuyuvulpes.yoamod.YOAMod.MODID;

public class BlockEntitiesModReg {

    public static final DeferredRegister<BlockEntityType<?>> BL_ENTITY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ForgingTableBlockEntity>> FORGING_TABLE = BL_ENTITY.register("forging_table",
            () -> BlockEntityType.Builder.of(ForgingTableBlockEntity::new,BlocksModReg.FORGING_TABLE.get()).build(null));

    public static void register(IEventBus eventBus){
        BL_ENTITY.register(eventBus);
    }
}
