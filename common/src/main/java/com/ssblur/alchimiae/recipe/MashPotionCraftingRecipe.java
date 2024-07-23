package com.ssblur.alchimiae.recipe;

import com.ssblur.alchimiae.item.AlchimiaeItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MashPotionCraftingRecipe extends CustomRecipe {
  public MashPotionCraftingRecipe(CraftingBookCategory craftingBookCategory) {
    super(craftingBookCategory);
  }

  boolean isWaterBottle(ItemStack item) {
    var data = item.get(DataComponents.POTION_CONTENTS);
    return data != null && data.is(Potions.WATER);
  }

  @Override
  public boolean matches(CraftingInput recipeInput, Level level) {
    return recipeInput.items().stream().filter(item -> item.is(AlchimiaeItems.MASH.get())).count() == 1
      && recipeInput.items().stream().filter(this::isWaterBottle).count() == 1
      && recipeInput.ingredientCount() == 2;
  }

  @Override
  public ItemStack assemble(CraftingInput recipeInput, HolderLookup.Provider provider) {
    var mash = recipeInput.items().stream().filter(item -> item.is(AlchimiaeItems.MASH.get())).findAny();
    if(mash.isEmpty()) return ItemStack.EMPTY;
    var item = new ItemStack(recipeInput.items().stream().filter(this::isWaterBottle).findAny().get().getItem());
    item.set(DataComponents.POTION_CONTENTS, mash.get().get(DataComponents.POTION_CONTENTS));
    item.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.potion"));
    return item;
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return i + j >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return AlchimiaeRecipes.MASH_POTION.get();
  }
}
