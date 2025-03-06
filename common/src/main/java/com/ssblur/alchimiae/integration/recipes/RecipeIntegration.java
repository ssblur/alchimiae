package com.ssblur.alchimiae.integration.recipes;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import com.ssblur.alchimiae.recipe.MashPotionCraftingRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Objects;

public class RecipeIntegration {
  public interface InformationRegistrar {
    void register(ResourceLocation location, List<ItemStack> items, Component... components);
  }

  public interface RecipesHolder {
    List<RecipeHolder<CraftingRecipe>> recipes();
  }

  public interface ShapelessRecipeRegistrar {
    void register(List<Ingredient> ingredients, ItemStack result, ResourceLocation id);
  }

  public static void registerItemInfo(InformationRegistrar informationRegistrar) {
    informationRegistrar.register(
      AlchimiaeMod.INSTANCE.location("grinder_info_grinder"),
      List.of(Ingredient.of(AlchimiaeItems.INSTANCE.getGRINDER()).getItems()),
      Component.translatable("info.alchimiae.grinder_1"),
      Component.translatable("info.alchimiae.grinder_2")
    );
    informationRegistrar.register(
      AlchimiaeMod.INSTANCE.location("grinder_info_mash"),
      List.of(new ItemStack(AlchimiaeItems.INSTANCE.getMASH())),
      Component.translatable("info.alchimiae.grinder_1"),
      Component.translatable("info.alchimiae.grinder_2")
    );

    informationRegistrar.register(
      AlchimiaeMod.INSTANCE.location("mash_info"),
      List.of(new ItemStack(AlchimiaeItems.INSTANCE.getMASH())),
      Component.translatable("info.alchimiae.mash_1"),
      Component.translatable("info.alchimiae.mash_2"),
      Component.translatable("info.alchimiae.mash_3")
    );
  }

  public static void registerRecipes(RecipesHolder recipesHolder, ShapelessRecipeRegistrar shapelessRecipeRegistrar) {
    var registrar = AlchimiaeItems.INSTANCE.getITEMS().getRegistrar();
    recipesHolder.recipes().forEach(holder -> {
      switch (holder.value()) {
        case MashPotionCraftingRecipe ignored -> {
          var potions = AlchimiaeMod.INSTANCE.getREGISTRIES().get().get(Registries.POTION);
          potions.entrySet().forEach(entry -> {
            var key = entry.getKey();
            var mash = PotionContents.createItemStack(AlchimiaeItems.INSTANCE.getMASH().get(), Objects.requireNonNull(potions.getHolder(key)));
            var potion = new ItemStack(Items.POTION);
            potion.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.potion"));
            potion.set(DataComponents.POTION_CONTENTS, mash.get(DataComponents.POTION_CONTENTS));
            shapelessRecipeRegistrar.register(
              List.of(
                Ingredient.of(mash),
                Ingredient.of(PotionContents.createItemStack(Items.POTION, Potions.WATER))
              ),
              potion,
              AlchimiaeMod.INSTANCE.location("mash_stuffing_").withSuffix(key.location().getPath())
            );
          });
        }
        default -> {}
      }
    });
  }
}
