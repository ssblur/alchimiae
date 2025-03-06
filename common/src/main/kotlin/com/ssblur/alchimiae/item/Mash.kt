package com.ssblur.alchimiae.item

import net.minecraft.core.component.DataComponents
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
    val potionContents = itemStack.get(DataComponents.POTION_CONTENTS)
    potionContents?.addPotionTooltip({ e: Component ->
      list.add(
        e
      )
    }, 1.0f, tooltipContext.tickRate())
  }

  companion object {
    fun getColor(item: ItemStack, layer: Int): Int {
      val data = item.get(DataComponents.POTION_CONTENTS)
      if (data != null) return data.color
      return -0x1
    }
  }
}
