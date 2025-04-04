package com.ssblur.alchimiae.integration.emi;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.block.AlchimiaeBlocks;
import com.ssblur.alchimiae.data.AlchimiaeDataComponents;
import com.ssblur.alchimiae.integration.recipes.RecipeIntegration;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Arrays;
import java.util.List;

@EmiEntrypoint
public class AlchimiaeEMIIntegration implements EmiPlugin {
  @Override
  public void register(EmiRegistry registry) {
    registry.addCategory(new EmiRecipeCategory(
            AlchimiaeMod.INSTANCE.location("boiler"),
            EmiIngredient.of(Ingredient.of(AlchimiaeBlocks.INSTANCE.getBOILER().component2().get())))
    );

    RecipeIntegration.registerItemInfo(((location, items, components) -> registry.addRecipe(new EmiInfoRecipe(
      items.stream().map(item -> EmiIngredient.of(Ingredient.of(item))).toList(),
      Arrays.stream(components).toList(),
      location
    ))));

    var manager = registry.getRecipeManager();
    RecipeIntegration.registerRecipes(() -> manager.getAllRecipesFor(RecipeType.CRAFTING), ((ingredients, result, id) -> registry.addRecipe(
      new EmiCraftingRecipe(
        ingredients.stream().map(EmiIngredient::of).toList(),
        EmiStack.of(result),
        id
      )
    )));

    registry.addRecipe(new EmiCraftingRecipe(
      List.of(
        EmiIngredient.of(Ingredient.of(AlchimiaeItems.INSTANCE.getGRINDER())),
        new IngredientEmiIngredient(9),
        new IngredientEmiIngredient(9),
        new IngredientEmiIngredient(8),
        new IngredientEmiIngredient(7),
        new IngredientEmiIngredient(6),
        new IngredientEmiIngredient(5),
        new IngredientEmiIngredient(4),
        new IngredientEmiIngredient(3)
      ),
      EmiStack.of(new ItemStack(AlchimiaeItems.INSTANCE.getMASH())),
      AlchimiaeMod.INSTANCE.location("mash_recipe")
    ));

    var potions = BuiltInRegistries.POTION;
    potions.entrySet().forEach(entry -> {
      var key = entry.getKey();

      var mash = new ItemStack(AlchimiaeItems.INSTANCE.getMASH().get());
      mash.set(AlchimiaeDataComponents.INSTANCE.getCUSTOM_POTION(), RecipeIntegration.convertPotion(entry.getValue()));
      registry.addRecipe(new EmiMashBrewingRecipe(mash, key.location().getPath()));
    });
  }


}
