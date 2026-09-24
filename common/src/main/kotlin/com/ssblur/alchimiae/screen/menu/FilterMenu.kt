package com.ssblur.alchimiae.screen.menu

import com.mojang.datafixers.util.Pair
import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.blockentity.FilterBlockEntity
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.item.potions.Mash
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class FilterMenu: AbstractContainerMenu {
  var filter: FilterBlockEntity? = null
  var data: ContainerData
  constructor(i: Int, inventory: Inventory, filterBlockEntity: FilterBlockEntity? = null) : super(AlchimiaeMenus.FILTER.get(), i) {
    val mx = 60 - (9 * 9) / 2
    val my = 94
    for (j in 0..2) {
      for (k in 0..8) {
        this.addSlot(Slot(inventory, k + j * 9 + 9, k * 18 + mx, j * 18 + my))
      }
    }

    for (j in 0..8) {
      this.addSlot(Slot(inventory, j, j * 18 + mx, my + 60))
    }

    filter = filterBlockEntity
    var container = filterBlockEntity ?: SimpleContainer(4)
    data = filterBlockEntity?.dataAccess ?: SimpleContainerData(1)

    this.addSlot(object: Slot(container, FilterBlockEntity.MASH_SLOT, 90, 20) {
      override fun mayPlace(itemStack: ItemStack): Boolean {
        return itemStack.item is Mash
      }

      override fun getNoItemIcon(): Pair<ResourceLocation?, ResourceLocation?> {
        return Pair(InventoryMenu.BLOCK_ATLAS, AlchimiaeMod.location("item/empty_mash"))
      }
    })
    this.addSlot(object: Slot(container, FilterBlockEntity.PAPER_SLOT, 90 - 24, 43) {
      override fun mayPlace(itemStack: ItemStack): Boolean {
        return itemStack matches Items.PAPER
      }

      override fun getNoItemIcon(): Pair<ResourceLocation?, ResourceLocation?> {
        return Pair(InventoryMenu.BLOCK_ATLAS, AlchimiaeMod.location("item/empty_paper"))
      }
    })
    this.addSlot(object: Slot(container, FilterBlockEntity.CHARCOAL_SLOT, 90 + 24, 43) {
      override fun mayPlace(itemStack: ItemStack): Boolean {
        return itemStack matches AlchimiaeItems.ACTIVATED_CHARCOAL.get()
      }

      override fun getNoItemIcon(): Pair<ResourceLocation?, ResourceLocation?> {
        return Pair(InventoryMenu.BLOCK_ATLAS, AlchimiaeMod.location("item/empty_charcoal"))
      }
    })
    this.addSlot(object: Slot(container, FilterBlockEntity.RESULT_SLOT, 90, 66) {
      override fun mayPlace(itemStack: ItemStack): Boolean = false
    })
    this.addDataSlots(data)
  }

  override fun quickMoveStack(
    player: Player,
    i: Int
  ): ItemStack? {
    return ItemStack.EMPTY
  }

  override fun stillValid(player: Player): Boolean {
    return filter != null && filter!!.stillValid(player)
  }
}