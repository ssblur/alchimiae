package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.screen.menu.AlchemyTableMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.StackedContents
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.StackedContentsCompatible
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState

class AlchemyTableBlockEntity(blockPos: BlockPos, blockState: BlockState) :
  BaseContainerBlockEntity(AlchimiaeBlockEntities.ALCHEMY_TABLE.get(), blockPos, blockState),
  WorldlyContainer, StackedContentsCompatible {
  var inventory: NonNullList<ItemStack> = NonNullList.withSize(11, ItemStack.EMPTY)

  override fun getDefaultName(): Component = Component.translatable("block.alchimiae.alchemy_table")
  override fun getItems(): NonNullList<ItemStack> = inventory
  override fun setItems(nonNullList: NonNullList<ItemStack>) {
    inventory = nonNullList
  }
  override fun createMenu(
    i: Int,
    inventory: Inventory
  ): AbstractContainerMenu = AlchemyTableMenu(i, inventory, this)
  override fun getContainerSize(): Int = 11
  override fun getSlotsForFace(direction: Direction): IntArray? {
    return when(direction) {
      Direction.UP -> (2..11).toList().toIntArray()
      Direction.DOWN -> intArrayOf(RESULT_SLOT)
      else -> intArrayOf(MORTAR_SLOT)
    }
  }
  override fun canPlaceItemThroughFace(
    i: Int,
    itemStack: ItemStack,
    direction: Direction?
  ): Boolean = direction != Direction.DOWN
  override fun canTakeItemThroughFace(
    i: Int,
    itemStack: ItemStack,
    direction: Direction
  ): Boolean = direction == Direction.DOWN
  override fun fillStackedContents(stackedContents: StackedContents) {
    for (itemStack in inventory) stackedContents.accountStack(itemStack)
  }

  // only process into result slot when it's clicked OR when the block gets a redstone signal

  companion object {
    const val MORTAR_SLOT = 0
    const val RESULT_SLOT = 1
  }
}