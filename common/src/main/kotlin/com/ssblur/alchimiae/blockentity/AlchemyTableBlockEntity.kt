package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.block.AlchemyTableBlock
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.screen.menu.AlchemyTableMenu
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.StackedContents
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.StackedContentsCompatible
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState

class AlchemyTableBlockEntity(blockPos: BlockPos, blockState: BlockState) :
  BaseContainerBlockEntity(AlchimiaeBlockEntities.ALCHEMY_TABLE.get(), blockPos, blockState),
  WorldlyContainer, StackedContentsCompatible {
  var inventory: NonNullList<ItemStack> = NonNullList.withSize(1, ItemStack.EMPTY)

  override fun getDefaultName(): Component = Component.translatable("block.alchimiae.alchemy_table")

  override fun getItems(): NonNullList<ItemStack> = inventory
  override fun setItems(nonNullList: NonNullList<ItemStack>) {
    inventory = nonNullList
  }
  override fun createMenu(
    i: Int,
    inventory: Inventory
  ): AbstractContainerMenu = AlchemyTableMenu(i, inventory, this)
  override fun getContainerSize(): Int = 1
  override fun getSlotsForFace(direction: Direction): IntArray {
    return intArrayOf(0)
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

  override fun stillValid(player: Player): Boolean {
    return blockPos.center.distanceTo(player.position()) < player.attributes.getValue(Attributes.BLOCK_INTERACTION_RANGE)
  }

  override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
    super.saveAdditional(compoundTag, provider)
    ContainerHelper.saveAllItems(compoundTag, inventory, provider)
  }

  override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
    super.loadAdditional(compoundTag, provider)
    ContainerHelper.loadAllItems(compoundTag, inventory, provider)
  }

  fun tick() {
    if(level?.isClientSide != false) return

    val grinder = inventory[0]
    val state = blockState.getValue(AlchemyTableBlock.MORTAR)
    if(grinder matches AlchimiaeItems.ALCHEMY_TABLE_GRINDER) {
      if(state != AlchemyTableBlock.Companion.AlchemyMortar.GRINDER)
        level?.setBlockAndUpdate(
          blockPos,
          blockState.setValue(
            AlchemyTableBlock.MORTAR,
            AlchemyTableBlock.Companion.AlchemyMortar.GRINDER
          )
        )
    } else if(grinder matches AlchimiaeItems.ALCHEMY_TABLE_MORTAR) {
      if(state != AlchemyTableBlock.Companion.AlchemyMortar.MORTAR)
        level?.setBlockAndUpdate(
          blockPos,
          blockState.setValue(
            AlchemyTableBlock.MORTAR,
            AlchemyTableBlock.Companion.AlchemyMortar.MORTAR
          )
        )
    } else {
      if(state != AlchemyTableBlock.Companion.AlchemyMortar.NONE)
        level?.setBlockAndUpdate(
          blockPos,
          blockState.setValue(
            AlchemyTableBlock.MORTAR,
            AlchemyTableBlock.Companion.AlchemyMortar.NONE
          )
        )
    }
  }

  companion object {
    const val MORTAR_SLOT = 0
  }
}