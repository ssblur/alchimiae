package com.ssblur.alchimiae.integration.jei;

import com.ssblur.alchimiae.item.AlchimiaeItems;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JeiMashBrewingRecipe implements IJeiBrewingRecipe {
  final ResourceLocation location;
  ItemStack mash;
  public JeiMashBrewingRecipe(ItemStack mash, String path) {
    this.mash = mash;
    this.location = itemId().withSuffix("_brewing_").withSuffix(path);
  }

  @Override
  public @Unmodifiable List<ItemStack> getPotionInputs() {
    return List.of(PotionContents.createItemStack(Items.POTION, Potions.WATER));
  }

  @Override
  public @Unmodifiable List<ItemStack> getIngredients() {
    return List.of(mash);
  }

  @Override
  public ItemStack getPotionOutput() {
    return potionResult();
  }

  @Override
  public int getBrewingSteps() {
    return 1;
  }

  @Override
  public @Nullable ResourceLocation getUid() {
    return location;
  }

  ItemStack potionResult() {
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
    return potion;
  }

  ResourceLocation itemId() {
    return Objects.requireNonNull(AlchimiaeItems.INSTANCE.getITEMS().getRegistrar().getId(mash.getItem()));
  }
}
