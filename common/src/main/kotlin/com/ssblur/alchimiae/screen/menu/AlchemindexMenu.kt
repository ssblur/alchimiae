package com.ssblur.alchimiae.screen.menu

import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class AlchemindexMenu(i: Int) : AbstractContainerMenu(AlchimiaeMenus.ALCHEMINDEX.get(), i) {
  override fun quickMoveStack(
    player: Player,
    i: Int
  ): ItemStack = ItemStack.EMPTY

  override fun stillValid(player: Player): Boolean = true
}