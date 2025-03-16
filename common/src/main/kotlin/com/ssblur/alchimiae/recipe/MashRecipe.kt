package com.ssblur.alchimiae.recipe

import com.ssblur.alchimiae.alchemy.AlchemyHelper
import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.item.GrinderItem
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import kotlin.random.Random

class MashRecipe(craftingBookCategory: CraftingBookCategory?) : CustomRecipe(craftingBookCategory) {
  var server: ServerLevel? = null
  override fun matches(recipeInput: CraftingInput, level: Level): Boolean {
    if (recipeInput.items().stream().noneMatch { item: ItemStack -> item.`is`(AlchimiaeItems.GRINDER) }) return false
    if (recipeInput.ingredientCount() <= 2) return false

    if (level is ServerLevel) {
      this.server = level
      val items = recipeInput.items().stream().filter { item: ItemStack -> !item.`is`(AlchimiaeItems.GRINDER) }.toList()

      return AlchemyHelper.getEffects(items, server!!, 1.0f) != null
    } else {
      return false
    }
  }

  override fun assemble(recipeInput: CraftingInput, provider: HolderLookup.Provider): ItemStack {
    val output = ItemStack(AlchimiaeItems.MASH)
    if (server == null || !server!!.server.isRunning) return output
    var efficiency = 0.5f
    val grinder = recipeInput.items().stream().filter { item: ItemStack -> item.`is`(AlchimiaeItems.GRINDER) }.findAny()
    if (grinder.isPresent && grinder.get().item is GrinderItem)
      efficiency = (grinder.get().item as GrinderItem).efficiency
    val items = recipeInput.items().stream().filter { item: ItemStack -> !item.`is`(AlchimiaeItems.GRINDER) }.toList()
    output.set(
      AlchimiaeDataComponents.CUSTOM_POTION,
      AlchemyHelper.getCustomPotionEffects(items, server!!, efficiency)
    )
    return output
  }

  override fun canCraftInDimensions(i: Int, j: Int): Boolean {
    return i + j >= 1
  }

  override fun getSerializer(): RecipeSerializer<*> {
    return AlchimiaeRecipes.MASH.get()
  }

  override fun getRemainingItems(recipeInput: CraftingInput): NonNullList<ItemStack> {
    val list = NonNullList.withSize(recipeInput.size(), ItemStack.EMPTY)
    for (i in 0..<recipeInput.size()) {
      var item = recipeInput.getItem(i).copy()
      var unbreaking = 0
      EnchantmentHelper.getEnchantmentsForCrafting(item).entrySet().forEach {
        if(it.key == Enchantments.UNBREAKING)
          unbreaking += it.intValue
      }

      if (item.`is`(AlchimiaeItems.GRINDER) && item.isDamageableItem) {
        if(Random.nextInt(10) > unbreaking) item.damageValue = item.damageValue + 1
        if (item.damageValue >= item.maxDamage) item = ItemStack.EMPTY
        list[i] = item
      }
    }
    return list
  }
}
