package com.ssblur.alchimiae.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.ssblur.alchimiae.AlchimiaeMod;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.HashMap;

public class IngredientReloadListener extends AlchimiaeReloadListener {
  public record IngredientResource(String item, String[] guaranteedEffects, String[] ingredientClasses, float rarity, int duration, boolean noEffects){}
  static Type EFFECT_TYPE = new TypeToken<IngredientResource>() {}.getType();
  public static final IngredientReloadListener INSTANCE = new IngredientReloadListener();

  public HashMap<ResourceLocation, IngredientResource> ingredients = new HashMap<>();

  public IngredientReloadListener() {
    super("alchimiae/ingredients");
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    IngredientResource resource = GSON.fromJson(jsonElement, EFFECT_TYPE);
    AlchimiaeMod.LOGGER.debug(
      "Loaded ingredient {} with:\n\tGuaranteed Effects: {}\n\tIngredient Classes: {}",
      resourceLocation,
      resource.guaranteedEffects,
      resource.ingredientClasses
    );
    ingredients.put(resourceLocation, resource);
  }
}
