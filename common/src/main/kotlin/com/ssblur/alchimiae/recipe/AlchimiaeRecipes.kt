package com.ssblur.alchimiae.recipe

import com.ssblur.alchimiae.AlchimiaeMod
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object AlchimiaeRecipes {
  val MASH = AlchimiaeMod.registerRecipeSerializer( "mash") {
    SimpleCraftingRecipeSerializer { craftingBookCategory: CraftingBookCategory? ->
      MashRecipe(
        craftingBookCategory
      )
    }
  }
  val MASH_POTION = AlchimiaeMod.registerRecipeSerializer("mash_potion") {
    SimpleCraftingRecipeSerializer { craftingBookCategory: CraftingBookCategory? ->
      MashPotionCraftingRecipe(
        craftingBookCategory
      )
    }
  }

  fun register() {}
}
