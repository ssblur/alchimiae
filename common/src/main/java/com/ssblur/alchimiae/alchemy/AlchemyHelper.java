package com.ssblur.alchimiae.alchemy;

import com.ssblur.alchimiae.data.IngredientEffectsSavedData;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AlchemyHelper {
  public static AlchemyPotion getEffects(List<AlchemyIngredient> ingredients, float efficiency) {
    HashMap<ResourceLocation, Float> potency = new HashMap<>();
    int duration = 0;
    for(var ingredient: ingredients) {
      for (var effect : ingredient.effects()) {
        potency.put(effect.effect(), potency.getOrDefault(effect.effect(), 0f) + effect.potency());
        duration += ingredient.duration();
      }
    }

    duration /= ingredients.size();
    duration *= 2;
    duration = Math.round(((float) duration) * efficiency);
    HashMap<ResourceLocation, Integer> output = new HashMap<>();
    potency.entrySet().stream()
      .filter(e -> e.getValue() >= 1)
      .forEach(e -> output.put(e.getKey(), ((int) Math.floor(Math.sqrt(e.getValue()))) - 1));
    return new AlchemyPotion(output, duration);
  }

  public static AlchemyPotion getEffects(List<ItemStack> items, ServerLevel level, float efficiency) {
    if(items.size() <= 1) return null;

    var ingredients = items.stream().filter(item -> !item.isEmpty()).map(
      ingredient -> IngredientEffectsSavedData.computeIfAbsent(level).getData().get(AlchimiaeItems.ITEMS.getRegistrar().getId(ingredient.getItem()))
    ).toList();

    if(ingredients.stream().anyMatch(Objects::isNull)) {
      return null;
    }

    for(int i = 0; i < items.size(); i++)
      for(int j = 0; j < items.size(); j++)
        if(j != i && !items.get(i).isEmpty() && !items.get(j).isEmpty() && items.get(i).getItem() == items.get(j).getItem()) {
          return null;
        }

    return getEffects(ingredients, efficiency);
  }

  public static List<MobEffectInstance> getPotion(List<ItemStack> items, ServerLevel level, float efficiency) {
    var effects = getEffects(items, level, efficiency);
    if(effects == null) return List.of();
    return effects.toPotion();
  }

  public static PotionContents getPotionContents(List<ItemStack> items, ServerLevel level) {
    return getPotionContents(items, level, 1.0f);
  }

  public static PotionContents getPotionContents(List<ItemStack> items, ServerLevel level, float efficiency) {
    return new PotionContents(Optional.empty(), Optional.empty(), getPotion(items, level, efficiency));
  }
}
