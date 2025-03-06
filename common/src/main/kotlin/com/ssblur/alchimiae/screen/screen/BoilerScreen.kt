package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.screen.menu.BoilerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory

class BoilerScreen(abstractContainerMenu: BoilerMenu, inventory: Inventory, component: Component) :
  AbstractContainerScreen<BoilerMenu?>(abstractContainerMenu, inventory, component) {
  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)

    this.renderTooltip(guiGraphics, i, j)
  }

  override fun renderBg(guiGraphics: GuiGraphics, f: Float, i: Int, j: Int) {
    var n: Int
    val k = this.leftPos
    val l = this.topPos
    guiGraphics.blit(TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight)

    menu?.let {
      if (it.isLit) {
        n = Mth.ceil((it.litProgress * 13.0f) + 1)
        guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - n, k + 56, l + 36 + 14 - n, 14, n)
      }

      n = Mth.ceil(it.burnProgress * 24.0f)
      guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, k + 79, l + 34, n, 16)
    }
  }

  companion object {
    private val TEXTURE: ResourceLocation = AlchimiaeMod.location("textures/gui/container/boiler.png")
    private val LIT_PROGRESS_SPRITE: ResourceLocation =
      ResourceLocation.withDefaultNamespace("container/furnace/lit_progress")
    private val BURN_PROGRESS_SPRITE: ResourceLocation =
      ResourceLocation.withDefaultNamespace("container/furnace/burn_progress")
  }
}
