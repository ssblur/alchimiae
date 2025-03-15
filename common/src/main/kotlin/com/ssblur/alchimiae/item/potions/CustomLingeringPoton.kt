package com.ssblur.alchimiae.item.potions

import com.ssblur.alchimiae.data.AlchimiaeDataComponents.CUSTOM_POTION
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.LingeringPotionItem
import net.minecraft.world.item.TooltipFlag

class CustomLingeringPoton(properties: Properties) : LingeringPotionItem(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
    list.removeLast()
    itemStack[CUSTOM_POTION]?.decorate(list)
  }
}