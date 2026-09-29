package com.ssblur.alchimiae.item

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class IdentifiedRecipe(properties: Properties) : Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component?>,
    tooltipFlag: TooltipFlag
  ) {
    itemStack[AlchimiaeDataComponents.POTION_RECIPE]?.decorateHoverText(itemStack, list)
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
  }

  override fun getName(itemStack: ItemStack): Component? {
    return itemStack[AlchimiaeDataComponents.POTION_RECIPE]?.name ?: super.getName(itemStack)
  }
}