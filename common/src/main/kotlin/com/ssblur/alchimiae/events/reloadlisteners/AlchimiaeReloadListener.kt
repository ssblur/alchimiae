package com.ssblur.alchimiae.events.reloadlisteners;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.ssblur.alchimiae.AlchimiaeMod;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public abstract class AlchimiaeReloadListener extends SimpleJsonResourceReloadListener {
  static Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  public AlchimiaeReloadListener(String string) {
    super(GSON, string);
  }

  public abstract void loadResource(ResourceLocation resourceLocation, JsonElement jsonElement);

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    object.forEach((resourceLocation, jsonElement) -> {
      if(jsonElement.isJsonObject()) {
        var jsonObject = jsonElement.getAsJsonObject();
        if(jsonObject.has("disabled") && jsonObject.get("disabled").getAsBoolean()) {
          AlchimiaeMod.LOGGER.debug("Did not load {}; disabled", resourceLocation);
          return;
        }
        if(jsonObject.has("required") && !Platform.isModLoaded(jsonObject.get("required").getAsString())) {
          AlchimiaeMod.LOGGER.debug("Did not load {}; missing required mod {}", resourceLocation, jsonObject.get("required").getAsString());
          return;
        }
      }
      loadResource(resourceLocation, jsonElement);
    });
  }
}
