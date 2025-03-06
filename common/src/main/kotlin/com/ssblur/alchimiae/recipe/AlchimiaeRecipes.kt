package com.ssblur.alchimiae.recipe;

import com.ssblur.alchimiae.AlchimiaeMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class AlchimiaeRecipes {
  public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.RECIPE_SERIALIZER);

  public static final RegistrySupplier<RecipeSerializer<?>> MASH = RECIPES.register("mash",
    () -> new SimpleCraftingRecipeSerializer<>(MashRecipe::new));
  public static final RegistrySupplier<RecipeSerializer<?>> MASH_POTION = RECIPES.register("mash_potion",
    () -> new SimpleCraftingRecipeSerializer<>(MashPotionCraftingRecipe::new));

  public static void register() {
    RECIPES.register();
  }
}
