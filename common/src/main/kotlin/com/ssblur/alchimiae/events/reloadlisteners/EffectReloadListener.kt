package com.ssblur.alchimiae.events.reloadlisteners;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.ssblur.alchimiae.AlchimiaeMod;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.HashMap;

public class EffectReloadListener extends AlchimiaeReloadListener {
  public record EffectResource(String effect, float rarity){}
  static Type EFFECT_TYPE = new TypeToken<EffectResource>() {}.getType();
  public static final EffectReloadListener INSTANCE = new EffectReloadListener();

  public HashMap<ResourceLocation, EffectResource> effects = new HashMap<>();

  public EffectReloadListener() {
    super("alchimiae/effects");
  }

  @Override
  public void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement) {
    EffectResource resource = GSON.fromJson(jsonElement, EFFECT_TYPE);
    AlchimiaeMod.LOGGER.debug(
      "Loaded effect {} with potion effect {} and rarity {}",
      resourceLocation,
      resource.effect,
      resource.rarity
    );
    effects.put(resourceLocation, resource);
  }
}
