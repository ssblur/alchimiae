package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.data.CustomPotionEffects
import com.ssblur.alchimiae.item.potions.Mash
import com.ssblur.alchimiae.screen.menu.FilterMenu
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.ContainerHelper
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState

class FilterBlockEntity(blockPos: BlockPos, blockState: BlockState) :
  BaseContainerBlockEntity(AlchimiaeBlockEntities.FILTER.get(), blockPos, blockState) {
  var inventory = NonNullList.withSize(4, ItemStack.EMPTY)
  var filterTime = 0
  val dataAccess: ContainerData = object : ContainerData {
    override fun get(i: Int): Int {
      if(i == 0) return filterTime
      return 0
    }

    override fun set(i: Int, j: Int) {
      if(i == 0) filterTime = j
    }

    override fun getCount(): Int {
      return 1
    }
  }
  override fun getDefaultName(): Component? {
    return Component.translatable("menu.alchimiae.filter")
  }

  override fun getItems(): NonNullList<ItemStack> {
    return inventory
  }

  override fun setItems(items: NonNullList<ItemStack?>) {
    inventory = items
  }

  override fun createMenu(
    i: Int,
    inventory: Inventory
  ): AbstractContainerMenu {
    return FilterMenu(i, inventory, this)
  }

  override fun getContainerSize(): Int {
    return 4
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
    if(inventory[PAPER_SLOT] matches Items.PAPER && inventory[MASH_SLOT].item is Mash) filterTime++
    else filterTime = 0

    if(filterTime >= FILTER_TIME) {
      filterTime = 0
      val out = inventory[MASH_SLOT].copyWithCount(1)
      val data = out[AlchimiaeDataComponents.CUSTOM_POTION] ?: return
      var effects = data.effects
      val count = if(inventory[CHARCOAL_SLOT].count > 0) 1 else 0
      for(i in 0..count) {
        val firstHarmful = effects.firstOrNull {
          val effect = BuiltInRegistries.MOB_EFFECT.get(it.location)
          effect?.category == MobEffectCategory.HARMFUL
        }
        effects = effects.filter { it != firstHarmful }
      }

      out[AlchimiaeDataComponents.CUSTOM_POTION] = CustomPotionEffects(
        effects,
        data.customColor,
        (data.filtered ?: 0) + 1
      )

      if(
        !inventory[RESULT_SLOT].isEmpty &&
        !ItemStack.isSameItemSameComponents(inventory[RESULT_SLOT], out)
      ) return

      if(inventory[RESULT_SLOT].isEmpty)
        inventory[RESULT_SLOT] = out
      else
        inventory[RESULT_SLOT].grow(1)

      inventory[PAPER_SLOT].shrink(1)
      inventory[MASH_SLOT].shrink(1)
      if(inventory[CHARCOAL_SLOT].count > 0) inventory[CHARCOAL_SLOT].shrink(1)

      level?.playSound(null, blockPos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS)
      setChanged()
    }
  }

  companion object {
    const val MASH_SLOT = 0
    const val PAPER_SLOT = 1
    const val CHARCOAL_SLOT = 2
    const val RESULT_SLOT = 3
    const val FILTER_TIME = 200
  }
}