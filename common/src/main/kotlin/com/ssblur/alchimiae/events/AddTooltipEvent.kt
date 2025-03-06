package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import dev.architectury.event.events.client.ClientTooltipEvent
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class AddTooltipEvent : ClientTooltipEvent.Item {
  override fun append(
    stack: ItemStack,
    lines: MutableList<Component>,
    tooltipContext: Item.TooltipContext,
    flag: TooltipFlag
  ) {
    val data = ClientAlchemyHelper.get(stack)
    if (data != null) {
      if (Screen.hasShiftDown()) {
        lines.add(Component.translatable("lore.alchimiae.ingredient").withStyle(ChatFormatting.AQUA))
        for (item in data) lines.add(
          Component.literal(" - ").append(
            Component.translatable(item).withStyle(
              ChatFormatting.BLUE
            )
          )
        )
      } else {
        lines.add(
          Component.translatable("lore.alchimiae.ingredient").withStyle(ChatFormatting.AQUA)
            .append(" ")
            .append(Component.translatable("lore.alchimiae.hold_shift").withStyle(ChatFormatting.GRAY))
        )
      }
    }
  }
}
