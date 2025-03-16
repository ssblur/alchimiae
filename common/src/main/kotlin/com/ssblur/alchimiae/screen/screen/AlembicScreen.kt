package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.screen.menu.AlembicMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory

class AlembicScreen(abstractContainerMenu: AlembicMenu, inventory: Inventory, component: Component) :
  AbstractContainerScreen<AlembicMenu?>(abstractContainerMenu, inventory, component) {
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
        guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - n, k + 22, l + 35 + 14 - n, 14, n)
      }

      n = Mth.ceil(it.burnProgress * 21.0f)
      guiGraphics.blitSprite(PROGRESS_SPRITE, 32, 32, 0, 0, k + 71, l + 34, 32, n)
//      guiGraphics.blitSprite(PROGRESS_SPRITE, k + 72, l + 34, 32, n)
    }
  }

  companion object {
    private val TEXTURE: ResourceLocation = AlchimiaeMod.location("textures/gui/container/alembic.png")
    private val PROGRESS_SPRITE: ResourceLocation = AlchimiaeMod.location("container/alembic/progress")
    private val LIT_PROGRESS_SPRITE: ResourceLocation =
      ResourceLocation.withDefaultNamespace("container/furnace/lit_progress")
  }
}
