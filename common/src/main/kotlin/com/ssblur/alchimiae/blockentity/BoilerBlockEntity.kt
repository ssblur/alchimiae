package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.data.CustomEffect
import com.ssblur.alchimiae.data.CustomPotionEffects
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.network.client.AlchimiaeNetworkS2C
import com.ssblur.alchimiae.screen.menu.BoilerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.StackedContents
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.StackedContentsCompatible
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.max
import kotlin.math.min

class BoilerBlockEntity(blockPos: BlockPos, blockState: BlockState) :
  BaseContainerBlockEntity(AlchimiaeBlockEntities.BOILER.get(), blockPos, blockState),
  WorldlyContainer, StackedContentsCompatible {
  var litTime: Int = 0
  var litDuration: Int = 0
  var processTime: Int = 0
  val dataAccess: ContainerData

  var inventory: NonNullList<ItemStack>

  init {
    inventory = NonNullList.withSize(3, ItemStack.EMPTY)

    this.dataAccess = object : ContainerData {
      override fun get(i: Int): Int {
        return when (i) {
          0 -> {
            litTime
          }

          1 -> {
            litDuration
          }

          2 -> {
            processTime
          }

          else -> {
            0
          }
        }
      }

      override fun set(i: Int, j: Int) {
        when (i) {
          0 -> litTime = j
          1 -> litDuration = j
          2 -> processTime = j
        }
      }

      override fun getCount(): Int {
        return 3
      }
    }
  }

  override fun getContainerSize(): Int {
    return 3
  }

  override fun getDefaultName(): Component {
    return Component.translatable("menu.alchimiae.boiler")
  }

  override fun getItems(): NonNullList<ItemStack> {
    return inventory
  }

  override fun setItems(nonNullList: NonNullList<ItemStack>) {
    inventory = nonNullList
  }

  override fun isEmpty(): Boolean {
    return inventory.isEmpty()
  }

  override fun getItem(i: Int): ItemStack {
    return inventory[i]
  }

  override fun removeItem(i: Int, j: Int): ItemStack {
    setChanged()

    val stack = inventory[i]
    if (j >= stack.count) {
      inventory[i] = ItemStack.EMPTY
      return stack
    }
    stack.shrink(j)
    return stack.copyWithCount(j)
  }

  override fun removeItemNoUpdate(i: Int): ItemStack {
    val stack = inventory[i]
    inventory[i] = ItemStack.EMPTY
    return stack
  }

  override fun setItem(i: Int, itemStack: ItemStack) {
    inventory[i] = itemStack
  }

  override fun stillValid(player: Player): Boolean {
    return player.distanceToSqr(blockPos.center) < 16
  }

  override fun clearContent() {
    inventory = NonNullList.withSize(3, ItemStack.EMPTY)
  }

  override fun createMenu(i: Int, inventory: Inventory): AbstractContainerMenu {
    return BoilerMenu(i, inventory, this)
  }

  override fun getSlotsForFace(direction: Direction): IntArray {
    return when (direction) {
      Direction.DOWN -> {
        intArrayOf(RESULT_SLOT)
      }

      Direction.UP -> {
        intArrayOf(INGREDIENT_SLOT)
      }

      else -> {
        intArrayOf(FUEL_SLOT)
      }
    }
  }

  override fun canPlaceItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction?): Boolean {
    return direction == null || Arrays.stream(getSlotsForFace(direction)).anyMatch { s: Int -> s == i }
  }

  override fun canTakeItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction): Boolean {
    return Arrays.stream(getSlotsForFace(direction)).anyMatch { s: Int -> s == i }
  }

  override fun fillStackedContents(stackedContents: StackedContents) {
    for (itemStack in inventory) stackedContents.accountStack(itemStack)
  }

  override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
    super.saveAdditional(compoundTag, provider)
    ContainerHelper.saveAllItems(compoundTag, inventory, provider)

    compoundTag.putInt("processTime", processTime)
    compoundTag.putInt("litTime", litTime)
    compoundTag.putInt("litDuration", litDuration)
  }

  override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
    super.loadAdditional(compoundTag, provider)
    ContainerHelper.loadAllItems(compoundTag, inventory, provider)

    processTime = compoundTag.getInt("processTime")
    litTime = compoundTag.getInt("litTime")
    litDuration = compoundTag.getInt("litDuration")
  }

  fun tick() {
    if (level == null) return
    if (litTime > 0) {
      if (litTime % 3 == 0) {
        val x = blockPos.x + (10.0 / 16.0) + level!!.random.nextDouble() / 10.0
        val y = blockPos.y + (5.0 / 16.0)
        val z = blockPos.z + (5.0 / 16.0) + level!!.random.nextDouble() / 10.0
        if (level is ServerLevel)
          AlchimiaeNetworkS2C.flameParticle(Vec3(x, y, z), (level as ServerLevel).players())
      }
      litTime--
    }

    if (inventory[INGREDIENT_SLOT].`is`(AlchimiaeItems.MASH.get())) {
      if (litTime == 0 && AbstractFurnaceBlockEntity.isFuel(inventory[FUEL_SLOT])) {
        litDuration = AbstractFurnaceBlockEntity.getFuel()[inventory[FUEL_SLOT].item]!! / 2
        litTime = litDuration
        inventory[FUEL_SLOT].shrink(1)
      }

      processTime =
        if (litTime > 0) min(
          (processTime + 1).toDouble(),
          PROCESS_TIME.toDouble()
        )
          .toInt()
        else max((processTime - 1).toDouble(), 0.0).toInt()

      if (processTime >= PROCESS_TIME) {
        val ingredient = inventory[INGREDIENT_SLOT]
        val data = ingredient[AlchimiaeDataComponents.CUSTOM_POTION]
        if (data != null) {
          val effects: MutableList<CustomEffect> = ArrayList()
          for ((location, duration, strength) in data.effects) {
            if(duration <= 1) {
              effects.add(CustomEffect(location, duration, strength))
            } else {
              effects.add(CustomEffect(location, duration / 4, strength + 1))
            }
          }
          val outputStack = ItemStack(AlchimiaeItems.CONCENTRATE)
          outputStack[AlchimiaeDataComponents.CUSTOM_POTION] = CustomPotionEffects(effects, data.customColor)

          if (inventory[RESULT_SLOT].isEmpty) {
            inventory[RESULT_SLOT] = outputStack
            inventory[INGREDIENT_SLOT].shrink(1)
            processTime = 0
          } else if (ItemStack.isSameItemSameComponents(
              outputStack,
              inventory[RESULT_SLOT]
            ) &&
            inventory[RESULT_SLOT].count < inventory[RESULT_SLOT].maxStackSize
          ) {
            inventory[RESULT_SLOT].grow(1)
            inventory[INGREDIENT_SLOT].shrink(1)
            processTime = 0
          }
        }
      }
    } else {
      processTime = max((processTime - 3).toDouble(), 0.0).toInt()
    }
  }

  companion object {
    const val INGREDIENT_SLOT: Int = 0
    const val FUEL_SLOT: Int = 1
    const val RESULT_SLOT: Int = 2
    const val PROCESS_TIME: Int = 400

    @Suppress("unused")
    fun tick(level: Level?, blockPos: BlockPos?, blockState: BlockState?, blockEntity: BlockEntity) {
      if (blockEntity is BoilerBlockEntity) blockEntity.tick()
    }
  }
}
