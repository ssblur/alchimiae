package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.block.AlembicBlock
import com.ssblur.alchimiae.network.client.AlchimiaeNetworkS2C
import com.ssblur.alchimiae.screen.menu.AlembicMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.ai.attributes.Attributes
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

class AlembicBlockEntity(blockPos: BlockPos, blockState: BlockState) :
  BaseContainerBlockEntity(AlchimiaeBlockEntities.ALEMBIC.get(), blockPos, blockState),
  WorldlyContainer, StackedContentsCompatible {
  var litTime: Int = 0
  var litDuration: Int = 0
  var processTime: Int = 0
  val dataAccess: ContainerData

  var inventory: NonNullList<ItemStack> = NonNullList.withSize(4, ItemStack.EMPTY)

  init {
    this.dataAccess = object : ContainerData {
      override fun get(i: Int): Int {
        return when (i) {
          0 -> litTime
          1 -> litDuration
          2 -> processTime
          else -> 0
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
    return 4
  }

  override fun getDefaultName(): Component {
    return Component.translatable("menu.alchimiae.alembic")
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
    return blockPos.center.distanceTo(player.position()) < player.attributes.getValue(Attributes.BLOCK_INTERACTION_RANGE)
  }

  override fun clearContent() {
    inventory = NonNullList.withSize(3, ItemStack.EMPTY)
  }

  override fun createMenu(i: Int, inventory: Inventory): AbstractContainerMenu {
    return AlembicMenu(i, inventory, this)
  }

  override fun getSlotsForFace(direction: Direction): IntArray {
    return when (direction) {
      Direction.DOWN -> intArrayOf(FIRST_POTION_SLOT, SECOND_POTION_SLOT)
      Direction.UP -> intArrayOf(INGREDIENT_SLOT, FIRST_POTION_SLOT, SECOND_POTION_SLOT)
      else -> intArrayOf(FUEL_SLOT)
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

  fun getFlamePos(): Vec3 {
    val xzPair = when (blockState.getValue(AlembicBlock.FACING)){
      Direction.NORTH -> 7.0 to 8.0
      Direction.SOUTH -> 9.0 to 8.0
      Direction.WEST -> 8.0 to 9.0
      Direction.EAST -> 8.0 to 7.0
      null -> 0.0 to 0.0
      else -> error("No Y Axs on Horizontal facing!")
    }
    val x = blockPos.x + (xzPair.first / 16.0) + level!!.random.nextDouble() / 10.0
    val y = blockPos.y + (5.0 / 16.0)
    val z = blockPos.z + (xzPair.second / 16.0) + level!!.random.nextDouble() / 10.0
    return Vec3(x,y,z)
  }

  fun tick() {
    if (level == null || level!!.isClientSide) return
    if (litTime > 0) {
      if (litTime % 3 == 0 && level is ServerLevel) {
          AlchimiaeNetworkS2C.flameParticle(getFlamePos(), (level as ServerLevel).players())
      }
      litTime--
    }

    if(litTime < 1 && fuelValueInSlot() > 0 && validRecipe()) {
      litDuration = fuelValueInSlot()
      litTime = litDuration
      inventory[FUEL_SLOT].shrink(1)
    }

    if(litTime > 0 && validRecipe()) {
      processTime++
    } else {
      processTime--
    }
    processTime = processTime.coerceIn(0..PROCESS_TIME)
    if(processTime >= PROCESS_TIME) {
      processTime = 0
      mix()
    }

    if(firstPotion.isEmpty == blockState.getValue(AlembicBlock.POTION1))
      level?.setBlockAndUpdate(blockPos, blockState.setValue(AlembicBlock.POTION1, !firstPotion.isEmpty))

    if(secondPotion.isEmpty == blockState.getValue(AlembicBlock.POTION2))
      level?.setBlockAndUpdate(blockPos, blockState.setValue(AlembicBlock.POTION2, !secondPotion.isEmpty))
  }

  fun fuelValueInSlot(): Int {
    return AbstractFurnaceBlockEntity.getFuel()[inventory[FUEL_SLOT].item] ?: 0
  }

  fun validRecipe(): Boolean {
    if(level != null)
      return level!!.potionBrewing().hasMix(firstPotion, ingredient)
          || level!!.potionBrewing().hasMix(secondPotion, ingredient)
    return false
  }

  fun mix() {
    if(level == null) return
    if(level!!.potionBrewing().hasMix(firstPotion, ingredient)) {
      firstPotion = level!!.potionBrewing().mix(ingredient, firstPotion)
    }
    if(level!!.potionBrewing().hasMix(secondPotion, ingredient)) {
      secondPotion = level!!.potionBrewing().mix(ingredient, secondPotion)
    }
    ingredient.shrink(1)
  }

  var firstPotion: ItemStack
    get() = inventory[FIRST_POTION_SLOT]
    set(value) {
      inventory[FIRST_POTION_SLOT] = value
    }

  var secondPotion: ItemStack
    get() = inventory[SECOND_POTION_SLOT]
    set(value) {
      inventory[SECOND_POTION_SLOT] = value
    }

  var ingredient: ItemStack
    get() = inventory[INGREDIENT_SLOT]
    set(value) {
      inventory[INGREDIENT_SLOT] = value
    }

  companion object {
    const val INGREDIENT_SLOT: Int = 0
    const val FUEL_SLOT: Int = 1
    const val FIRST_POTION_SLOT: Int = 2
    const val SECOND_POTION_SLOT: Int = 3
    const val PROCESS_TIME: Int = 400

    @Suppress("unused_parameter")
    fun tick(level: Level?, blockPos: BlockPos?, blockState: BlockState?, blockEntity: BlockEntity) {
      if (blockEntity is AlembicBlockEntity) blockEntity.tick()
    }
  }
}
