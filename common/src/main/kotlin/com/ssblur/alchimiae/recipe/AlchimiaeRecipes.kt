package com.ssblur.alchimiae.recipe

import com.ssblur.alchimiae.AlchimiaeMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object AlchimiaeRecipes {
  val RECIPES: DeferredRegister<RecipeSerializer<*>> =
    DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.RECIPE_SERIALIZER)

  val MASH: RegistrySupplier<RecipeSerializer<*>> = RECIPES.register(
    "mash"
  ) {
    SimpleCraftingRecipeSerializer { craftingBookCategory: CraftingBookCategory? ->
      MashRecipe(
        craftingBookCategory
      )
    }
  }
  val MASH_POTION: RegistrySupplier<RecipeSerializer<*>> = RECIPES.register(
    "mash_potion"
  ) {
    SimpleCraftingRecipeSerializer { craftingBookCategory: CraftingBookCategory? ->
      MashPotionCraftingRecipe(
        craftingBookCategory
      )
    }
  }

  fun register() {
    RECIPES.register()
  }
}
