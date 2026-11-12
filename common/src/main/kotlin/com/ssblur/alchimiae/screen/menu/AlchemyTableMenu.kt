package com.ssblur.alchimiae.screen.menu

import com.ssblur.alchimiae.blockentity.AlchemyTableBlockEntity
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.recipe.AlchimiaeRecipes
import com.ssblur.alchimiae.resource.Ingredients
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

class AlchemyTableMenu(i: Int, val inventory: Inventory? = null, val blockEntity: AlchemyTableBlockEntity? = null) : AbstractContainerMenu(AlchimiaeMenus.ALCHEMY_TABLE.get(), i)  {
  val resultSlot: Slot
  val craftingContainer: TransientCraftingContainer
  val resultContainer: ResultContainer
  val grinderSlot: Slot
  val player: Player?
  init {
    val mx = 50 - (9 * 9) / 2
    val my = 94

    var xo = 46
    var yo = 18
    val container = blockEntity ?: SimpleContainer(11)
    craftingContainer = object: TransientCraftingContainer(this, 3, 4) {
      override fun setItem(i: Int, itemStack: ItemStack) {
        super.setItem(i, itemStack)
      }

      override fun getItem(i: Int): ItemStack? {
        if(i == 9) return super.getItem(i).copy()
        return super.getItem(i)
      }
    }
    resultContainer = ResultContainer()
    player = inventory?.player

    grinderSlot = this.addSlot(object: Slot(container, AlchemyTableBlockEntity.MORTAR_SLOT, xo - 21, yo + 18) {
      override fun mayPlace(itemStack: ItemStack): Boolean {
        return itemStack matches AlchimiaeItems.GRINDER
      }

      override fun setChanged() {
        super.setChanged()
        this@AlchemyTableMenu.updateCraftingRecipe()
        if(player?.level()?.isClientSide == false) {
          craftingContainer.setItem(9, this.item.copy())
          this@AlchemyTableMenu.updateCraftingRecipe()
        }
      }
    })
    craftingContainer.setItem(9, grinderSlot.item.copy())

    for(i in 0..8) {
      this.addSlot(object: Slot(craftingContainer, i, xo + 18*(i%3), yo + (i/3)*18) {
        override fun mayPlace(itemStack: ItemStack): Boolean {
          return itemStack matches Ingredients.INGREDIENTS
        }

        override fun setChanged() {
          super.setChanged()
          this@AlchemyTableMenu.updateCraftingRecipe()
        }

        override fun isFake(): Boolean = false
      })
    }
    xo += 18 * 3 + 26

    resultSlot = if(player?.level()?.isClientSide == false) object: ResultSlot(player, craftingContainer, resultContainer,10, xo + 9, yo + 18) {
      override fun onTake(player: Player, itemStack: ItemStack) {
        super.onTake(player, itemStack)

        if(!grinderSlot.item.isEmpty) {
          grinderSlot.item.damageValue += 1
          if (grinderSlot.item.damageValue >= grinderSlot.item.maxDamage) grinderSlot.item.shrink(1)
        }
        craftingContainer.setItem(9, grinderSlot.item.copy())
        this@AlchemyTableMenu.updateCraftingRecipe()
      }
    } else object: Slot(container, 10, xo + 9, yo + 18) {
      override fun mayPlace(itemStack: ItemStack): Boolean = false
    }
    this.addSlot(resultSlot)

    if(inventory == null) assert(false)
    val playerInventory = inventory ?: SimpleContainer(36)
    for (j in 0..2) {
      for (k in 0..8) {
        this.addSlot(Slot(playerInventory, k + j * 9 + 9, k * 18 + mx, j * 18 + my))
      }
    }

    for (j in 0..8) {
      this.addSlot(Slot(playerInventory, j, j * 18 + mx, my + 60))
    }
  }

  override fun quickMoveStack(
    player: Player,
    i: Int
  ): ItemStack? {
    val slot = slots[i]
    if(slot.hasItem()) {
      val item = slot.item
      if(i == 10) {
        item.item.onCraftedBy(item, player.level(), player)
        if (!moveItemStackTo(item, 11, 36 + 10, true)) {
          return ItemStack.EMPTY
        }

        resultSlot.onTake(player, item)
        slot.onQuickCraft(item, item.copy())
        return item.copy()
      }

      updateCraftingRecipe()

      if(i > 10) { // player's inventory
        if(moveItemStackTo(item, 0, 11, false)) {
          updateCraftingRecipe()
          return item.copy()
        }
        return ItemStack.EMPTY
      } else { // machine inventory
        if (moveItemStackTo(item, 11, 36 + 10, true)) {
          updateCraftingRecipe()
          return item.copy()
        }
        return ItemStack.EMPTY
      }
    }
    return ItemStack.EMPTY
  }

  override fun stillValid(player: Player): Boolean = blockEntity?.stillValid(player) ?: false

  fun updateCraftingRecipe() {
    blockEntity?.level?.let { it as? ServerLevel }?.let {
      var out = ItemStack.EMPTY
      val input = craftingContainer.asCraftInput()
      val optional: Optional<RecipeHolder<CraftingRecipe?>?> = it.server.recipeManager
        .getRecipeFor(RecipeType.CRAFTING, input, it, AlchimiaeRecipes.MASH.key?.location())
      optional.ifPresent { result ->
        if(resultContainer.setRecipeUsed(it, player as ServerPlayer, result)) {
          out = result.value?.assemble(input, it.registryAccess()) ?: ItemStack.EMPTY
        }
      }

      resultContainer.setItem(10, out)
      this.setRemoteSlot(10, out)
      (player as ServerPlayer).connection.send(
        ClientboundContainerSetSlotPacket(
          this.containerId,
          this.incrementStateId(),
          10,
          out
        )
      )
    }
  }

  override fun removed(player: Player) {
    super.removed(player)
    craftingContainer.setItem(9, ItemStack.EMPTY)
    clearContainer(player, craftingContainer)
  }

  override fun canTakeItemForPickAll(itemStack: ItemStack, slot: Slot): Boolean {
    if(slot.container == craftingContainer && slot.index == 9) return false
    if(slot.container == resultContainer) return false
    return super.canTakeItemForPickAll(itemStack, slot)
  }
}