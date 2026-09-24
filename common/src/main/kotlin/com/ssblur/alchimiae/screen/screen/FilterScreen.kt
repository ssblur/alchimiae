package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.screen.menu.FilterMenu
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.renderable.InventoryBackground
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class FilterScreen(val filterMenu: FilterMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<FilterMenu>(filterMenu, inventory, component) {
  override fun init() {
    super.init()
    imageWidth = 200
    imageHeight = 180
    leftPos = (width - imageWidth) / 2
    topPos = (height - imageHeight) / 2
    inventoryLabelY = imageHeight - 98
    add(InventoryBackground(leftPos, topPos, imageWidth, imageHeight))
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)
    renderSlotBackgrounds(guiGraphics, *filterMenu.slots.toTypedArray())

    guiGraphics.drawString(this.font, "${filterMenu.data[0]}", leftPos + 90, topPos + 48, 4210752, false)
  }

  override fun renderBackground(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.renderBackground(guiGraphics, i, j, f)
  }

  override fun renderLabels(guiGraphics: GuiGraphics, i: Int, j: Int) {
    guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false)
    guiGraphics.drawString(
      this.font,
      this.playerInventoryTitle,
      this.inventoryLabelX,
      this.inventoryLabelY,
      4210752,
      false
    )
  }
}