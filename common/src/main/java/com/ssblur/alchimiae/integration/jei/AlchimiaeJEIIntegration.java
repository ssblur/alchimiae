package com.ssblur.alchimiae.integration.jei;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

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

    registration.addItemStackInfo(
      List.of(Ingredient.of(AlchimiaeItems.GRINDER).getItems()),
      Component.translatable("info.alchimiae.grinder_1"),
      Component.translatable("info.alchimiae.grinder_2")
    );
    registration.addItemStackInfo(
      new ItemStack(AlchimiaeItems.MASH),
      Component.translatable("info.alchimiae.grinder_1"),
      Component.translatable("info.alchimiae.grinder_2")
    );

    registration.addItemStackInfo(
      new ItemStack(AlchimiaeItems.MASH),
      Component.translatable("info.alchimiae.mash_1"),
      Component.translatable("info.alchimiae.mash_2"),
      Component.translatable("info.alchimiae.mash_3")
    );
  }
}
