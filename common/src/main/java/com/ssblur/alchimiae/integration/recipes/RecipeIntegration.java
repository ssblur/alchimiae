package com.ssblur.alchimiae.integration.recipes;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.data.AlchimiaeDataComponents;
import com.ssblur.alchimiae.data.CustomEffect;
import com.ssblur.alchimiae.data.CustomPotionEffects;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import com.ssblur.alchimiae.recipe.MashPotionCraftingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
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
    recipesHolder.recipes().forEach(holder -> {
      switch (holder.value()) {
        case MashPotionCraftingRecipe ignored -> {
          var potions = Minecraft.getInstance().level.registryAccess().registry(Registries.POTION).get();

          potions.entrySet().forEach(entry -> {
            var key = entry.getKey();
            var mash = new ItemStack(AlchimiaeItems.INSTANCE.getMASH().get());
            mash.set(AlchimiaeDataComponents.INSTANCE.getCUSTOM_POTION(), convertPotion(entry.getValue()));
            var potion = new ItemStack(AlchimiaeItems.INSTANCE.getPOTION());
            potion.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.potion"));
            potion.set(AlchimiaeDataComponents.INSTANCE.getCUSTOM_POTION(), mash.get(AlchimiaeDataComponents.INSTANCE.getCUSTOM_POTION()));
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
}
