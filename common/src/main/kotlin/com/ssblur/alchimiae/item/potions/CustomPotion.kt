package com.ssblur.alchimiae.item.potions

import com.ssblur.alchimiae.alchemy.AlchemyHelper
import com.ssblur.alchimiae.data.AlchimiaeDataComponents.CUSTOM_POTION
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.PotionItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class CustomPotion(properties: Properties) : PotionItem(properties) {
  override fun finishUsingItem(itemStack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
    AlchemyHelper.applyEffects(itemStack, livingEntity)
    return super.finishUsingItem(itemStack, level, livingEntity)
  }

  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
    itemStack[CUSTOM_POTION]?.decorate(list)
  }

  companion object {
    fun getColor(itemStack: ItemStack, layer: Int): Int {
      if(layer == 0) return itemStack[CUSTOM_POTION]?.color ?: -0x1
      return 0xffffffffu.toInt()
    }
  }
}