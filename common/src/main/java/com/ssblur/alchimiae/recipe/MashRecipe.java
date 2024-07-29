package com.ssblur.alchimiae.recipe;

import com.ssblur.alchimiae.alchemy.AlchemyHelper;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MashRecipe extends CustomRecipe {
  ServerLevel server;
  public MashRecipe(CraftingBookCategory craftingBookCategory) {
    super(craftingBookCategory);
  }

  @Override
  public boolean matches(CraftingInput recipeInput, Level level) {
    if(recipeInput.items().stream().noneMatch(item -> item.is(AlchimiaeItems.GRINDER)))
      return false;
    if(recipeInput.ingredientCount() <= 2)
      return false;

    if(level instanceof ServerLevel serverLevel) {
      this.server = serverLevel;
      var items = recipeInput.items().stream().filter(item -> !item.is(AlchimiaeItems.GRINDER)).toList();

      return AlchemyHelper.getEffects(items, server) != null;
    } else {
      return false;
    }
  }

  @Override
  public ItemStack assemble(CraftingInput recipeInput, HolderLookup.Provider provider) {
    var output = new ItemStack(AlchimiaeItems.MASH);
    if(server == null || !server.getServer().isRunning()) return output;
    var items = recipeInput.items().stream().filter(item -> !item.is(AlchimiaeItems.GRINDER)).toList();
    output.set(
      DataComponents.POTION_CONTENTS,
      AlchemyHelper.getPotionContents(items, server)
    );
    return output;
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return i + j >= 1;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return AlchimiaeRecipes.MASH.get();
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput recipeInput) {
    NonNullList<ItemStack> list = NonNullList.withSize(recipeInput.size(), ItemStack.EMPTY);
    for(int i = 0; i < recipeInput.size(); i++) {
      var item = recipeInput.getItem(i);
      if(item.is(AlchimiaeItems.GRINDER) && item.isDamageableItem()) {
        item.setDamageValue(item.getDamageValue() + 1);
        if(item.getDamageValue() >= item.getMaxDamage())
          item = ItemStack.EMPTY;
        list.set(i, item.copy());
      }
    }
    return list;
  }
}
