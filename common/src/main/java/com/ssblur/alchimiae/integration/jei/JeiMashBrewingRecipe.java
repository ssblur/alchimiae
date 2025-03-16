package com.ssblur.alchimiae.integration.jei;

import com.ssblur.alchimiae.data.AlchimiaeDataComponents;
import com.ssblur.alchimiae.data.CustomEffect;
import com.ssblur.alchimiae.data.CustomPotionEffects;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    var potion = mash.transmuteCopy(AlchimiaeItems.INSTANCE.getPOTION().get());
    var contents = mash.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    var effects = new ArrayList<CustomEffect>();
    contents.getAllEffects().forEach(effect ->
            effects.add(new CustomEffect(
                    effect.getEffect().unwrapKey().get().location(),
                    effect.getDuration(),
                    effect.getAmplifier()
            ))
    );
    potion.set(AlchimiaeDataComponents.INSTANCE.getCUSTOM_POTION(), new CustomPotionEffects(effects, null));
    potion.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.potion"));
    return potion;
  }

  ResourceLocation itemId() {
    return Objects.requireNonNull(Minecraft.getInstance().level.registryAccess().registry(Registries.ITEM).get().getKey(mash.getItem()));
  }
}
