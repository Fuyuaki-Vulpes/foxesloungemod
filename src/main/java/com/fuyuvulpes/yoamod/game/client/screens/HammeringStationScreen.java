package com.fuyuvulpes.yoamod.game.client.screens;

import com.fuyuvulpes.yoamod.YOAMod;
import com.fuyuvulpes.yoamod.game.server.crafting.HammeringStationRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
    @OnlyIn(Dist.CLIENT)
    public class HammeringStationScreen extends AbstractContainerScreen<HammeringStationMenu> {
        private static final ResourceLocation BG_LOCATION = new ResourceLocation(YOAMod.MODID,"textures/gui/container/hammering_station.png");
        private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation(YOAMod.MODID,"container/hammering_station/scroller");
        private static final ResourceLocation SCROLLER_DISABLED_SPRITE = new ResourceLocation(YOAMod.MODID,"container/hammering_station/scroller_disabled");
        private static final ResourceLocation RECIPE_SELECTED_SPRITE = new ResourceLocation(YOAMod.MODID,"container/hammering_station/recipe_selected");
        private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = new ResourceLocation(YOAMod.MODID,"container/hammering_station/recipe_highlighted");
        private static final ResourceLocation RECIPE_SPRITE = new ResourceLocation(YOAMod.MODID,"container/hammering_station/recipe");
        private float scrollOffs;
        private boolean scrolling;
        private int startIndex;
        private boolean displayRecipes;

        public HammeringStationScreen(HammeringStationMenu containerIn, Inventory playerInv, Component title) {
            super(containerIn, playerInv, title);
            containerIn.registerUpdateListener(this::containerChanged);
            this.titleLabelY = 10000;
            this.inventoryLabelY = 10000;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            renderTooltip(guiGraphics, mouseX, mouseY);
        }

        @Override
        protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
            int i = this.leftPos;
            int j = this.topPos;
            guiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
            int k = (int) (41f * this.scrollOffs);
            guiGraphics.blitSprite(isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE, i + 116, j + 8 + k, 12, 15);
            int l = this.leftPos + 49;
            int i1 = this.topPos + 7;
            int j1 = this.startIndex + 12;
            renderButtons(guiGraphics, mouseX, mouseY, l, i1, j1);
            renderRecipes(guiGraphics, l, i1, j1);
        }

        @Override
        protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
            super.renderTooltip(guiGraphics, x, y);
            if (this.displayRecipes) {
                int i = this.leftPos + 49;
                int j = this.topPos + 7;
                int k = this.startIndex + 12;
                List<RecipeHolder<HammeringStationRecipe>> list = this.menu.getRecipes();
                for (int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); ++l) {
                    int i1 = l - this.startIndex;
                    int j1 = i + i1 % 4 * 16;
                    int k1 = j + i1 / 4 * 18 + 2;
                    if (x >= j1 && x < j1 + 16 && y >= k1 && y < k1 + 18) {
                        assert  this.minecraft != null && this.minecraft.level != null;
                        guiGraphics.renderTooltip(this.font, list.get(l).value().getResultItem(this.minecraft.level.registryAccess()), x, y);
                    }
                }
            }
        }

        private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int scrollOffset) {
            for (int i = this.startIndex; i < scrollOffset && i < this.menu.getNumRecipes(); ++i) {
                int j = i - this.startIndex;
                int k = x + j % 4 * 16;
                int l = j / 4;
                int i1 = y + l * 18 + 2;
                ResourceLocation rl;
                if (i == this.menu.getSelectedRecipeIndex()) {
                    rl = RECIPE_SELECTED_SPRITE;
                } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                    rl = RECIPE_HIGHLIGHTED_SPRITE;
                } else {
                    rl = RECIPE_SPRITE;
                }
                guiGraphics.blitSprite(rl, k, i1 - 1, 16, 18);
            }
        }

        private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int scrollOffset) {
            List<RecipeHolder<HammeringStationRecipe>> list = this.menu.getRecipes();
            for (int i = this.startIndex; i < scrollOffset && i < this.menu.getNumRecipes(); ++i) {
                int j = i - this.startIndex;
                int k = x + j % 4 * 16;
                int l = j / 4;
                int i1 = y + l * 18 + 2;
                assert  this.minecraft != null && this.minecraft.level != null;
                guiGraphics.renderItem(list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), k, i1);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
            this.scrolling = false;
            if (this.displayRecipes) {
                int i = this.leftPos + 49;
                int j = this.topPos + 7;
                int k = this.startIndex + 12;
                for (int l = this.startIndex; l < k; ++l) {
                    int i1 = l - this.startIndex;
                    double d0 = mouseX - (double) (i + i1 % 4 * 16);
                    double d1 = mouseY - (double) (j + i1 / 4 * 18);
                    assert this.minecraft != null && this.minecraft.player != null;
                    if (d0 >= 0d && d1 >= 0d && d0 < 16d && d1 < 18d && this.menu.clickMenuButton(this.minecraft.player, l)) {
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ANVIL_USE, 1f));
                        assert this.minecraft.gameMode != null;
                        this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
                        return true;
                    }
                }
                i = this.leftPos + 116;
                j = this.topPos + 8;
                if (mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 54)) {
                    this.scrolling = true;
                    // allows to click on the slide bar to change the position (without drag)
                    this.scrollOffs = Mth.clamp((float) (mouseY - j - 7.5f) / 40f, 0f, 1f);
                    this.startIndex = (int) ((double) (this.scrollOffs * (float) getOffscreenRows()) + 0.5d) * 4;
                }
            }
            return super.mouseClicked(mouseX, mouseY, buttonId);
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int buttonId, double dragX, double dragY) {
            if (this.scrolling && isScrollBarActive()) {
                int i = this.topPos + 7;
                int j = i + 49;
                this.scrollOffs = ((float) mouseY - (float) i - 7.5f) / ((float) (j - i) - 15f);
                this.scrollOffs = Mth.clamp(this.scrollOffs, 0f, 1f);
                this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5d) * 4;
                return true;
            } else {
                return super.mouseDragged(mouseX, mouseY, buttonId, dragX, dragY);
            }
        }

        @Override
        public boolean mouseScrolled(double p_99314_, double p_99315_, double p_99316_, double p_297300_) {
            if (isScrollBarActive()) {
                int i = getOffscreenRows();
                float f = (float) p_297300_ / (float) i;
                this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0f, 1f);
                this.startIndex = (int) ((double) (this.scrollOffs * (float) i) + 0.5d) * 4;
            }
            return true;
        }

        private boolean isScrollBarActive() {
            return this.displayRecipes && this.menu.getNumRecipes() > 12;
        }

        private int getOffscreenRows() {
            return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
        }

        private void containerChanged() {
            this.displayRecipes = this.menu.hasInputItem();
            if (!this.displayRecipes) {
                this.scrollOffs = 0f;
                this.startIndex = 0;
            }
        }

        @Override
        protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
            // reset the slider bar if 2 items are switched from the input slot
            if (type == ClickType.PICKUP && slotId == 0 && mouseButton == 0 && slotIn != null && slotIn.index == 0) {
                if (!this.menu.getCarried().isEmpty() && !slotIn.getItem().isEmpty() && !ItemStack.isSameItemSameTags(this.menu.getCarried(), slotIn.getItem())) {
                    this.scrollOffs = 0f;
                    this.startIndex = 0;
                }
            }
            super.slotClicked(slotIn, slotId, mouseButton, type);
        }
    }

