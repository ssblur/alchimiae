package com.ssblur.alchimiae.integration.jei;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.integration.recipes.RecipeIntegration;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class AlchimiaeJEIIntegration implements IModPlugin {
  public static final IIngredientType<TagKey> ITEM_TAG = new IIngredientType<>() {
    public String getUid() {
      return "alchimiae:item_tag";
    }

    public Class<? extends TagKey> getIngredientClass() {
      return TagKey.class;
    }
  };

  @Override
  public ResourceLocation getPluginUid() {
    return AlchimiaeMod.location("jei_base");
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    IModPlugin.super.registerRecipeCatalysts(registration);
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    IModPlugin.super.registerIngredients(registration);
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IModPlugin.super.registerCategories(registration);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    IModPlugin.super.registerRecipes(registration);

    var level = Minecraft.getInstance().level;
    assert level != null;
    var manager = level.getRecipeManager();

    RecipeIntegration.registerItemInfo((location, itemStacks, components) -> {
      registration.addItemStackInfo(
        itemStacks,
        components
      );
    });

    RecipeIntegration.registerRecipes(() -> manager.getAllRecipesFor(RecipeType.CRAFTING), ((ingredients, result, id) -> {
      var list = NonNullList.withSize(ingredients.size() + 1, Ingredient.of(ItemStack.EMPTY));
      for(int i = 0; i < ingredients.size(); i++)
        list.set(i, ingredients.get(i));

      registration.addRecipes(
        RecipeTypes.CRAFTING,
        List.of(
          new RecipeHolder<>(
            id,
            new ShapelessRecipe(
              id.getPath(),
              CraftingBookCategory.MISC,
              result,
              list
            )
          )
        )
      );
    }));

    var potions = AlchimiaeMod.REGISTRIES.get().get(Registries.POTION);
    potions.entrySet().forEach(entry -> {
      var key = entry.getKey();
      var mash = PotionContents.createItemStack(AlchimiaeItems.MASH.get(), Objects.requireNonNull(potions.getHolder(key)));
      registration.addRecipes(RecipeTypes.BREWING, List.of(new JeiMashBrewingRecipe(mash, key.location().getPath())));
    });
  }
}
