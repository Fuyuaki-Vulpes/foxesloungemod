package com.fuyuvulpes.yoamod.game.client.screens;

import com.fuyuvulpes.yoamod.core.registries.BlocksModReg;
import com.fuyuvulpes.yoamod.core.registries.MenusModReg;
import com.fuyuvulpes.yoamod.world.entity.block.CrucibleBlockEntity;
import com.mojang.serialization.Decoder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CrucibleMenu extends AbstractContainerMenu {

    public final CrucibleBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public CrucibleMenu(int pId, Inventory pPlayer) {
        this(pId, pPlayer, new SimpleContainer(5), pPlayer.player.level().getBlockEntity(pPlayer.), new SimpleContainerData(5));
    }

    public CrucibleMenu(int pId, Inventory pPlayer, FriendlyByteBuf extraData) {
        this(pId, pPlayer, new SimpleContainer(5), pPlayer.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5));
    }

    public CrucibleMenu(int pId, Inventory pPlayer, Container pContainer,  BlockEntity entity, ContainerData data) {
        super(MenusModReg.CRUCIBLE_MENU.get(), pId);
        checkContainerSize(pPlayer, 2);
        blockEntity = ((CrucibleBlockEntity) entity);
        this.level = pPlayer.player.level();
        this.data = data;

        addPlayerInventory(pPlayer);
        addPlayerHotbar(pPlayer);

        this.addSlot(new Slot(pContainer, 0, 23, 19));
        this.addSlot(new Slot(pContainer, 1, 41, 19));
        this.addSlot(new Slot(pContainer, 2, 32, 41));
        this.addSlot(new Slot(pContainer, 3, 80, 51));
        this.addSlot(new Slot(pContainer, 4, 128, 19));


        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 62; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public boolean isLit() {
        return data.get(0) > 0;
    }

    public int getScaledUsage() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressFuelSize = 10; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressFuelSize / maxProgress : 0;
    }



    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 6;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, BlocksModReg.CRUCIBLE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}

