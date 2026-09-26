package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.blockentity.FilterBlockEntity
import com.ssblur.alchimiae.screen.menu.FilterMenu
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.renderable.InventoryBackground
import com.ssblur.unfocused.screen.renderable.VerticalProgressGraphic
import com.ssblur.unfocused.screen.widget.HelpButtonWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class FilterScreen(val filterMenu: FilterMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<FilterMenu>(filterMenu, inventory, component) {
  override fun init() {
    super.init()
    imageWidth = 180
    imageHeight = 180
    leftPos = (width - imageWidth) / 2
    topPos = (height - imageHeight) / 2
    inventoryLabelY = imageHeight - 98
    inventoryLabelX += 2
    titleLabelX = inventoryLabelX
    add(InventoryBackground(leftPos, topPos, imageWidth, imageHeight))
    add(HelpButtonWidget(
      leftPos + imageWidth - 20, topPos + 8, 12, 12,
      AlchimiaeMod.location("filter")
    ))
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)

    val progress = filterMenu.data[0].toDouble() / FilterBlockEntity.FILTER_TIME.toDouble()
    VerticalProgressGraphic.draw(
      guiGraphics,
      leftPos + 73,
      topPos + 30,
      34,
      36,
      PROGRESS_BACKGROUND,
      PROGRESS_FOREGROUND,
      progress,
      true
    )

    renderSlotBackgrounds(guiGraphics, *filterMenu.slots.toTypedArray())
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

  companion object {
    val PROGRESS_BACKGROUND = AlchimiaeMod.location("container/filter/progress_background")
    val PROGRESS_FOREGROUND = AlchimiaeMod.location("container/filter/progress_foreground")
  }
}