package com.ssblur.alchimiae.screen.screen.widget

import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ItemButtonWidget(x: Int, y: Int, val size: Int, val item: Item, val onclick: Runnable):
  PositionedWidget(x, y, size, size, scissor = false) {
  val itemStack = ItemStack(item)
  init {
    pageButtons = false
    maxScroll = 0
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, itemStack.displayName)
  }

  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    guiGraphics.renderFakeItem(itemStack, 0, 0)
    if(hovered) {
      guiGraphics.fill(0, 0, size, size, 0x66ffffffu.toInt())
      guiGraphics.renderTooltip(Minecraft.getInstance().font, itemStack, size, 0)
    }
  }

  override fun leftClick(x: Double, y: Double): Boolean {
    onclick.run()
    return true
  }
}