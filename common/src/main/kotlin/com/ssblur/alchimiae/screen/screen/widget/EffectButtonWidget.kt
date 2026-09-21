package com.ssblur.alchimiae.screen.screen.widget

import com.ssblur.alchimiae.mixin.TextureAtlasHolderAccessor
import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class EffectButtonWidget(x: Int, y: Int, val size: Int, val effect: ResourceLocation, val onclick: Runnable):
  PositionedWidget(x, y, size, size, scissor = false) {
  init {
    pageButtons = false
    maxScroll = 0
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(effect.toLanguageKey("effect")))
  }

  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    val textures = Minecraft.getInstance().mobEffectTextures as TextureAtlasHolderAccessor
    var sprite = textures.invokeGetSprite(effect)
    val missing = textures.invokeGetSprite(missingEffect)
    if (sprite.contents().name() == missingno) sprite = missing
    guiGraphics.blit(0, 0, 0, size, size, sprite)
    if(hovered) {
      guiGraphics.fill(0, 0, size, size, 0x66ffffffu.toInt())
      guiGraphics.renderComponentTooltip(
        Minecraft.getInstance().font,
        listOf(Component.translatable(effect.toLanguageKey("effect"))),
        size,
        0
      )
    }
  }

  override fun leftClick(x: Double, y: Double): Boolean {
    onclick.run()
    return true
  }

  companion object {
    val missingEffect = ResourceLocation.parse("alchimiae:missing")
    var missingno = ResourceLocation.parse("minecraft:missingno")
  }
}