package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.alchimiae.screen.menu.AlchemyTableMenu
import com.ssblur.alchimiae.screen.screen.widget.ClearButtonWidget
import com.ssblur.alchimiae.screen.screen.widget.EffectButtonWidget
import com.ssblur.unfocused.extension.SoundEventExtension.play
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.renderable.InventoryBackground
import com.ssblur.unfocused.screen.widget.HelpButtonWidget
import com.ssblur.unfocused.screen.widget.PlainTextWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Inventory
import kotlin.math.min

class AlchemyTableScreen(val menu: AlchemyTableMenu, inventory: Inventory, component: Component):
  UnfocusedScreen<AlchemyTableMenu>(menu, inventory, component) {
  var filter: ResourceLocation? = null

  init {
    inventoryLabelX += 2
  }

  override fun init() {
    super.init()
    imageWidth = 180
    imageHeight = 180
    leftPos = (width - imageWidth) / 2
    topPos = (height - imageHeight) / 2
    inventoryLabelY = imageHeight - 98
    titleLabelX = inventoryLabelX
    addBackground(InventoryBackground(leftPos, topPos, imageWidth, imageHeight))
    add(HelpButtonWidget(
      leftPos + imageWidth - 20, topPos + 8, 12, 12,
      AlchimiaeMod.location("alchemy_table")
    ))

    val font = Minecraft.getInstance().font
    val effectWidth = min(leftPos, EFFECTS_WIDTH)
    val fontWidth = font.width(Component.translatable("gui.alchimiae.effects_filter"))
    var y = topPos + 6
    add(PlainTextWidget(
      Component.translatable("gui.alchimiae.effects_filter"),
      leftPos - effectWidth / 2 - fontWidth / 2,
      y,
      fontWidth,
      12,
      false
    )).let { it.color = 0xffffffffu }

    y += 12

    var x = leftPos - effectWidth
    add(ClearButtonWidget(x, y, EFFECT_SIZE) {
      rebuildWidgets()
      SoundEvents.UI_BUTTON_CLICK.play()
      filter = null
    })
    x += EFFECT_SIZE + EFFECT_SPACING

    val effects = ClientAlchemyHelper.EFFECTS.values.flatMap { it?.toList() ?: listOf() }
      .distinct()
      .sortedBy {
        I18n.get(it.toLanguageKey("effect"))
      }
    for(effect in effects) {
      add(EffectButtonWidget(x, y, EFFECT_SIZE, effect) {
        rebuildWidgets()
        SoundEvents.UI_BUTTON_CLICK.play()
        filter = effect
      })
      x += EFFECT_SIZE + EFFECT_SPACING
      if((x + EFFECT_SIZE) >= leftPos) {
        y += EFFECT_SIZE + EFFECT_SPACING
        x = leftPos - effectWidth
      }
    }
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)

    if(filter != null)
      for(slot in menu.slots) {
        if(slot == menu.resultSlot || slot == menu.grinderSlot) continue
        if(ClientAlchemyHelper.get(slot.item)?.contains(filter) != true || slot.item.isEmpty)
          guiGraphics.fill(
            leftPos + slot.x,
            topPos + slot.y,
            leftPos + slot.x + 16,
            topPos + slot.y + 16,
            0xaa000000u.toInt()
          )
      }
  }

  override fun renderBackground(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.renderBackground(guiGraphics, i, j, f)
    renderSlotBackgrounds(guiGraphics, *menu.slots.toTypedArray())
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
    const val EFFECTS_WIDTH = 128
    const val EFFECT_SIZE = 16
    const val EFFECT_SPACING = 2
  }
}