package com.ssblur.alchimiae.integration.recipes;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.data.AlchimiaeDataComponents;
import com.ssblur.alchimiae.data.CustomEffect;
import com.ssblur.alchimiae.data.CustomPotionEffects;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import com.ssblur.alchimiae.recipe.MashPotionCraftingRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

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
      infoLocation("grinder"),
      List.of(Ingredient.of(AlchimiaeItems.INSTANCE.getGRINDER()).getItems()),
      Component.translatable("info.alchimiae.grinder_1"),
      Component.translatable("info.alchimiae.grinder_2")
    );
    informationRegistrar.register(
      infoLocation("mash_grinder"),
      List.of(defaultMash()),
      Component.translatable("info.alchimiae.grinder_1"),
      Component.translatable("info.alchimiae.grinder_2")
    );
    informationRegistrar.register(
      infoLocation("mash"),
      List.of(defaultMash()),
      Component.translatable("info.alchimiae.mash_1"),
      Component.translatable("info.alchimiae.mash_2"),
      Component.translatable("info.alchimiae.mash_3")
    );
  }

  public static void registerRecipes(RecipesHolder recipesHolder, ShapelessRecipeRegistrar shapelessRecipeRegistrar) {
    recipesHolder.recipes().forEach(holder -> {
      switch (holder.value()) {
        case MashPotionCraftingRecipe ignored -> {
            var potion = defaultMash().transmuteCopy(AlchimiaeItems.INSTANCE.getPOTION().get());
            potion.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.potion"));
            shapelessRecipeRegistrar.register(
              List.of(
                Ingredient.of(defaultMash()),
                Ingredient.of(PotionContents.createItemStack(Items.POTION, Potions.WATER))
              ),
              potion,
              AlchimiaeMod.INSTANCE.location("/mash_stuffing")
            );
        }
        default -> {}
      }
    });
  }

  public static CustomPotionEffects convertPotion(Potion potion) {
    var effects = potion.getEffects().stream().map(instance ->
            new CustomEffect(
                    instance.getEffect().unwrapKey().get().location(),
                    instance.getDuration(),
                    instance.getAmplifier()
            )
    ).toList();
    return new CustomPotionEffects(effects, null);
  }

  public static ItemStack defaultMash(){
    var mash = new ItemStack(AlchimiaeItems.INSTANCE.getMASH().get());
    mash.set(AlchimiaeDataComponents.INSTANCE.getCUSTOM_POTION(), RecipeIntegration.convertPotion(Potions.HEALING.value()));
    return mash;
  }

  public static ResourceLocation infoLocation(String string){
    return AlchimiaeMod.INSTANCE.location("/"+string+"_info");
  }
}
