package com.ssblur.alchimiae.tooltip

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.alchimiae.mixin.TextureAtlasHolderAccessor
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
data class IngredientClientTooltipComponent(val tooltip: IngredientTooltipComponent): ClientTooltipComponent {
  override fun getHeight(): Int {
    return if(tooltip.shift) 66 else 14
  }

  override fun getWidth(font: Font): Int {
    if(tooltip.shift)
      return tooltip.effects.map { font.width(I18n.get("effect." + it.toLanguageKey())) }.max() + 20
    return 52 + font.width(I18n.get("lore.alchimiae.hold_shift"))
  }

  val missingEffect = ResourceLocation.parse("alchimiae:missing")
  var missingno = ResourceLocation.parse("minecraft:missingno")
  override fun renderImage(font: Font, i: Int, j: Int, guiGraphics: GuiGraphics) {
    super.renderImage(font, i, j, guiGraphics)
    val textures = Minecraft.getInstance().mobEffectTextures as TextureAtlasHolderAccessor
    val missing = textures.invokeGetSprite(missingEffect)
    var x = i - 2
    var y = j
    for(effect in tooltip.effects) {
      var sprite = textures.invokeGetSprite(effect)
      if (sprite.contents().name() == missingno) sprite = missing
      if(tooltip.shift) {
        guiGraphics.drawString(
          font,
          Component.translatable("effect." + effect.toLanguageKey()),
          x + 18,
          y + 5,
          ClientAlchemyHelper.getDispositionStyle(effect).color ?: 0xffffffffu.toInt()
        )
        guiGraphics.blit(x, y, 0, 16, 16, sprite)
        y += 16
      } else {
        guiGraphics.blit(x, y, 0, 12, 12, sprite)
        x += 13
      }
    }
    if(!tooltip.shift) {
      guiGraphics.drawString(
        font,
        Component.translatable("lore.alchimiae.hold_shift"),
        x + 1,
        y + 3,
        ChatFormatting.GRAY.color!!
      )
    }
  }
}