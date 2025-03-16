package com.ssblur.alchimiae.screen.menu

import com.ssblur.alchimiae.blockentity.AlembicBlockEntity
import net.minecraft.util.Mth
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity

class AlembicMenu : AbstractContainerMenu {
  var alembicBlockEntity: AlembicBlockEntity? = null
  var data: ContainerData

  constructor(i: Int, inventory: Inventory) : super(AlchimiaeMenus.ALEMBIC.get(), i) {
    val container = SimpleContainer(4)
    this.addSlot(Slot(container, FUEL, 21, 51))
    this.addSlot(Slot(container, INGREDIENT, 79, 17))
    this.addSlot(BrewingStandMenu.PotionSlot(container, POTION_1, 102, 51))
    this.addSlot(BrewingStandMenu.PotionSlot(container, POTION_2, 56, 51))

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

  constructor(i: Int, inventory: Inventory, alembicBlockEntity: AlembicBlockEntity) : super(
    AlchimiaeMenus.ALEMBIC.get(),
    i
  ) {
    this.addSlot(Slot(alembicBlockEntity, FUEL, 21, 51))
    this.addSlot(Slot(alembicBlockEntity, INGREDIENT, 79, 17))
    this.addSlot(BrewingStandMenu.PotionSlot(alembicBlockEntity, POTION_1, 102, 51))
    this.addSlot(BrewingStandMenu.PotionSlot(alembicBlockEntity, POTION_2, 56, 51))

    for (j in 0..2) {
      for (k in 0..8) {
        this.addSlot(Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18))
      }
    }

    for (j in 0..8) {
      this.addSlot(Slot(inventory, j, 8 + j * 18, 142))
    }

    this.alembicBlockEntity = alembicBlockEntity
    addDataSlots(alembicBlockEntity.dataAccess)
    data = alembicBlockEntity.dataAccess
  }

  override fun quickMoveStack(player: Player, i: Int): ItemStack {
    val slot = slots[i]
    if (slot.hasItem()) {
      val item = slot.item
      if (i > 3) { // If in the player's inventory.
        if (BrewingStandMenu.PotionSlot.mayPlaceItem(item)) {
          return if (moveItemStackTo(item, POTION_1, POTION_1 + 1, false)) {
            item.copy()
          } else if (moveItemStackTo(item, POTION_2, POTION_2 + 1, false)) {
            item.copy()
          } else {
            ItemStack.EMPTY
          }
        } else if (AbstractFurnaceBlockEntity.isFuel(item)) {
          return if (moveItemStackTo(item, INGREDIENT, INGREDIENT + 1, false)) {
            item.copy()
          } else {
            ItemStack.EMPTY
          }
        } else {
          return if (moveItemStackTo(item, FUEL, FUEL + 1, false)) {
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

    return ItemStack.EMPTY
  }

  override fun stillValid(player: Player): Boolean {
    return alembicBlockEntity?.stillValid(player) ?: false
  }

  val burnProgress: Float
    get() = Mth.clamp(
      data[2]
        .toFloat() / AlembicBlockEntity.PROCESS_TIME.toFloat(), 0.0f, 1.0f
    )

  val litProgress: Float
    get() = Mth.clamp(
      data[0].toFloat() / data[1]
        .toFloat(), 0.0f, 1.0f
    )

  val isLit: Boolean
    get() = data[0] > 0

  companion object {
    val POTION_1 = AlembicBlockEntity.FIRST_POTION_SLOT
    val POTION_2 = AlembicBlockEntity.SECOND_POTION_SLOT
    val FUEL = AlembicBlockEntity.FUEL_SLOT
    val INGREDIENT = AlembicBlockEntity.INGREDIENT_SLOT
  }
}
