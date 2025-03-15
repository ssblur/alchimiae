package com.ssblur.alchimiae.item.potions

import com.ssblur.alchimiae.data.AlchimiaeDataComponents.CUSTOM_POTION
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class Mash(properties: Properties) : Item(properties) {
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
    fun getColor(item: ItemStack, layer: Int): Int {
      val data = item[CUSTOM_POTION]
      return data?.color ?: -0x1
    }
  }
}
