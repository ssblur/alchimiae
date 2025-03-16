package com.ssblur.alchimiae.recipe

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.item.potions.Mash
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.LingeringPotionItem
import net.minecraft.world.item.PotionItem
import net.minecraft.world.item.SplashPotionItem
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class MashPotionCraftingRecipe(craftingBookCategory: CraftingBookCategory) : CustomRecipe(craftingBookCategory) {
  fun isWaterBottle(item: ItemStack): Boolean {
    val data = item.get(DataComponents.POTION_CONTENTS)
    val isPotion = item.item is PotionItem
    return isPotion && data != null && data.`is`(Potions.WATER)
  }

  override fun matches(recipeInput: CraftingInput, level: Level): Boolean {
    return recipeInput.items().stream().filter { item: ItemStack -> item.item is Mash }
      .count() == 1L && recipeInput.items().stream().filter { item: ItemStack -> this.isWaterBottle(item) }
      .count() == 1L && recipeInput.ingredientCount() == 2
  }

  override fun assemble(recipeInput: CraftingInput, provider: HolderLookup.Provider): ItemStack {
    val mash = recipeInput.items().stream().filter { item: ItemStack -> item.item is Mash }.findAny()
    if (mash.isEmpty) return ItemStack.EMPTY
    val option = recipeInput.items().stream().filter { item: ItemStack -> this.isWaterBottle(item) }.findAny()
    if (option.isEmpty) return ItemStack.EMPTY
    val item = ItemStack(when(option.get().item) {
      is LingeringPotionItem -> AlchimiaeItems.LINGERING_POTION.get()
      is SplashPotionItem -> AlchimiaeItems.SPLASH_POTION.get()
      else -> AlchimiaeItems.POTION.get()
    })
    item[AlchimiaeDataComponents.CUSTOM_POTION] =  mash.get()[AlchimiaeDataComponents.CUSTOM_POTION]
    item[DataComponents.ITEM_NAME] = Component.translatable("item.alchimiae.potion")
    return item
  }

  override fun canCraftInDimensions(i: Int, j: Int): Boolean {
    return i + j > 2
  }

  override fun getSerializer(): RecipeSerializer<*> {
    return AlchimiaeRecipes.MASH_POTION.get()
  }
}
