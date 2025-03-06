package com.ssblur.alchimiae.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record EmiMashBrewingRecipe(ItemStack mash, String potionPath) implements EmiRecipe {
  static ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/container/brewing_stand.png");
  static EmiStack BLAZE_POWDER = EmiStack.of(Items.BLAZE_POWDER);

  ResourceLocation itemId() {
    return Objects.requireNonNull(Minecraft.getInstance().level.registryAccess().registry(Registries.ITEM).get().getKey(mash.getItem()));
  }

  EmiStack potionResult() {
    var potion = mash.transmuteCopy(Items.POTION);
    var contents = mash.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    var effects = new ArrayList<MobEffectInstance>();
    contents.getAllEffects().forEach(effect ->
      effects.add(new MobEffectInstance(effect.getEffect(), (int) Math.round(effect.getDuration() * 0.6666), effect.getAmplifier()))
    );
    potion.set(DataComponents.POTION_CONTENTS, new PotionContents(
      Optional.empty(),
      contents.customColor(),
      effects
    ));
    potion.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.potion"));
    return EmiStack.of(potion);
  }

  static EmiStack waterIngredient() {
    var stack = PotionContents.createItemStack(Items.POTION, Potions.WATER);
    return EmiStack.of(stack);
  }

  EmiIngredient mashIngredient() {
    return EmiIngredient.of(Ingredient.of(mash));
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return VanillaEmiRecipeCategories.BREWING;
  }

  @Override
  public ResourceLocation getId() {
    return itemId().withSuffix("_brewing_").withSuffix(potionPath);
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return List.of(waterIngredient(), mashIngredient());
  }

  @Override
  public List<EmiStack> getOutputs() {
    return List.of(potionResult());
  }

  @Override
  public int getDisplayWidth() {
    return 120;
  }

  @Override
  public int getDisplayHeight() {
    return 61;
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(BACKGROUND, 0, 0, 103, 61, 16, 14);
    widgets.addAnimatedTexture(BACKGROUND, 81, 2, 9, 28, 176, 0, 1000 * 20, false, false, false)
      .tooltip(List.of(
        ClientTooltipComponent.create(Component.translatable("emi.cooking.time", 20).getVisualOrderText())
      ));
    widgets.addAnimatedTexture(BACKGROUND, 47, 0, 12, 29, 185, 0, 700, false, true, false);
    widgets.addTexture(BACKGROUND, 44, 30, 18, 4, 176, 29);
    widgets.addSlot(BLAZE_POWDER, 0, 2).drawBack(false);
    widgets.addSlot(waterIngredient(), 39, 36).drawBack(false);
    widgets.addSlot(mashIngredient(), 62, 2).drawBack(false);
    widgets.addSlot(potionResult(), 85, 36).drawBack(false).recipeContext(this);
  }
}
