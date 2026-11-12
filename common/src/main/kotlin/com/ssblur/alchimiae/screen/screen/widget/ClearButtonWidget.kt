package com.ssblur.alchimiae.screen.screen.widget

import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class ClearButtonWidget(x: Int, y: Int, val size: Int, val onclick: Runnable):
  PositionedWidget(x, y, size, size, scissor = false) {
  init {
    pageButtons = false
    maxScroll = 0
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("gui.alchimiae.clear_filter"))
  }

  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    guiGraphics.blitSprite(TEXTURE, 0, 0, size, size)
    if(hovered) {
      guiGraphics.fill(0, 0, size, size, 0x66ffffffu.toInt())
      guiGraphics.renderComponentTooltip(
        Minecraft.getInstance().font,
        listOf(Component.translatable("gui.alchimiae.clear_filter")),
        -Minecraft.getInstance().font.width(Component.translatable("gui.alchimiae.clear_filter"))/2,
        0
      )
    }
  }

  override fun leftClick(x: Double, y: Double): Boolean {
    onclick.run()
    return true
  }

  companion object {
    val TEXTURE = ResourceLocation.parse("alchimiae:container/clear")
  }
}