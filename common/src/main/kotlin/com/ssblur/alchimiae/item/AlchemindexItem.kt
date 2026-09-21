package com.ssblur.alchimiae.item

import com.ssblur.alchimiae.screen.menu.AlchemindexMenu
import com.ssblur.unfocused.menu.SimpleMenuProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AlchemindexItem: Item(Properties()) {
  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack?>? {
    player.openMenu(SimpleMenuProvider { i, _, _ ->
      AlchemindexMenu(i)
    })
    return super.use(level, player, interactionHand)
  }

  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    list.add(Component.translatable("info.alchimiae.alchemindex"))
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
  }
}