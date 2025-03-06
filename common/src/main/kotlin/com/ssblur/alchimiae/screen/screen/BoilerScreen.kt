package com.ssblur.alchimiae.screen.screen;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.screen.menu.BoilerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class BoilerScreen extends AbstractContainerScreen<BoilerMenu> {
  private static final ResourceLocation TEXTURE = AlchimiaeMod.location("textures/gui/container/boiler.png");
  private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");
  private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

  public BoilerScreen(BoilerMenu abstractContainerMenu, Inventory inventory, Component component) {
    super(abstractContainerMenu, inventory, component);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int i, int j, float f) {
    super.render(guiGraphics, i, j, f);

    this.renderTooltip(guiGraphics, i, j);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
    int n;
    int k = this.leftPos;
    int l = this.topPos;
    guiGraphics.blit(TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight);


    if (menu.isLit()) {
      n = Mth.ceil((menu.getLitProgress() * 13.0f) + 1);
      guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - n, k + 56, l + 36 + 14 - n, 14, n);
    }
    n = Mth.ceil(menu.getBurnProgress() * 24.0f);
    guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, k + 79, l + 34, n, 16);
  }
}
