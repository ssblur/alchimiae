package com.ssblur.alchimiae.screen.menu

import com.ssblur.alchimiae.blockentity.AlchemyTableBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class AlchemyTableMenu(i: Int, inventory: Inventory? = null, container: AlchemyTableBlockEntity? = null) : AbstractContainerMenu(AlchimiaeMenus.ALCHEMY_TABLE.get(), i)  {
  override fun quickMoveStack(
    player: Player,
    i: Int
  ): ItemStack? {
    TODO("Not yet implemented")
  }

  override fun stillValid(player: Player): Boolean {
    TODO("Not yet implemented")
  }
}