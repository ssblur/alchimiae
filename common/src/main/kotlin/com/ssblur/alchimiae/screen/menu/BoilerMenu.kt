package com.ssblur.alchimiae.screen.menu

import com.ssblur.alchimiae.blockentity.BoilerBlockEntity
import com.ssblur.alchimiae.item.AlchimiaeItems
import net.minecraft.util.Mth
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity

class BoilerMenu : AbstractContainerMenu {
  var boilerBlockEntity: BoilerBlockEntity? = null
  var data: ContainerData

  constructor(i: Int, inventory: Inventory) : super(AlchimiaeMenus.BOILER.get(), i) {
    val container = SimpleContainer(3)
    this.addSlot(Slot(container, 0, 56, 17))
    this.addSlot(Slot(container, 1, 56, 53))
    this.addSlot(FurnaceResultSlot(inventory.player, container, 2, 116, 35))

    for (j in 0..2) {
      for (k in 0..8) {
        this.addSlot(Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18))
      }
    }

    for (j in 0..8) {
      this.addSlot(Slot(inventory, j, 8 + j * 18, 142))
    }

    data = SimpleContainerData(3)
    addDataSlots(data)
  }

  constructor(i: Int, inventory: Inventory, boilerBlockEntity: BoilerBlockEntity) : super(
    AlchimiaeMenus.BOILER.get(),
    i
  ) {
    this.addSlot(Slot(boilerBlockEntity, 0, 56, 17))
    this.addSlot(Slot(boilerBlockEntity, 1, 56, 53))
    this.addSlot(FurnaceResultSlot(inventory.player, boilerBlockEntity, 2, 116, 35))

    for (j in 0..2) {
      for (k in 0..8) {
        this.addSlot(Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18))
      }
    }

    for (j in 0..8) {
      this.addSlot(Slot(inventory, j, 8 + j * 18, 142))
    }

    this.boilerBlockEntity = boilerBlockEntity
    addDataSlots(boilerBlockEntity.dataAccess)
    data = boilerBlockEntity.dataAccess
  }

  override fun quickMoveStack(player: Player, i: Int): ItemStack {
    val itemStack = ItemStack.EMPTY
    val slot = slots[i]
    if (slot.hasItem()) {
      val item = slot.item
      if (i > 2) { // If in the player's inventory.
        if (item.`is`(AlchimiaeItems.MASH.get())) {
          return if (moveItemStackTo(item, 0, 1, false)) {
            item.copy()
          } else {
            ItemStack.EMPTY
          }
        } else if (AbstractFurnaceBlockEntity.isFuel(item)) {
          return if (moveItemStackTo(item, 1, 2, false)) {
            item.copy()
          } else {
            ItemStack.EMPTY
          }
        }
      } else { // If in machine inventory
        return if (moveItemStackTo(item, 3, 39, true)) {
          item.copy()
        } else {
          ItemStack.EMPTY
        }
      }
    }

    return itemStack
  }

  override fun stillValid(player: Player): Boolean {
    if (boilerBlockEntity != null) return boilerBlockEntity!!.stillValid(player)
    return false
  }

  val burnProgress: Float
    get() = Mth.clamp(
      data[2]
        .toFloat() / BoilerBlockEntity.PROCESS_TIME.toFloat(), 0.0f, 1.0f
    )

  val litProgress: Float
    get() = Mth.clamp(
      data[0].toFloat() / data[1]
        .toFloat(), 0.0f, 1.0f
    )

  val isLit: Boolean
    get() = data[0] > 0
}
