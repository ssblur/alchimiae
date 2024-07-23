package com.ssblur.alchimiae.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.ssblur.alchimiae.AlchimiaeMod;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.HashMap;

public class IngredientClassReloadListener extends AlchimiaeReloadListener {
  public record ClassResource(float rarity, String[] guaranteedEffects){}
  static Type EFFECT_TYPE = new TypeToken<ClassResource>() {}.getType();
  public static final IngredientClassReloadListener INSTANCE = new IngredientClassReloadListener();

  HashMap<ResourceLocation, ClassResource> classes = new HashMap<>();

  public IngredientClassReloadListener() {
    super("alchimiae/classes");
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    ClassResource resource = GSON.fromJson(jsonElement, EFFECT_TYPE);
    AlchimiaeMod.LOGGER.debug(
      "Loaded ingredient class {}",
      resourceLocation
    );
    classes.put(resourceLocation, resource);
  }
}
