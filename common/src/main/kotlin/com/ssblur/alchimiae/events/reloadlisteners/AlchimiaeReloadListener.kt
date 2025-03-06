package com.ssblur.alchimiae.events.reloadlisteners

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.ssblur.alchimiae.AlchimiaeMod
import dev.architectury.platform.Platform
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller

abstract class AlchimiaeReloadListener(string: String?) :
  SimpleJsonResourceReloadListener(GSON, string) {
  abstract fun loadResource(resourceLocation: ResourceLocation, jsonElement: JsonElement?)

  override fun apply(
    `object`: Map<ResourceLocation?, JsonElement?>,
    resourceManager: ResourceManager,
    profilerFiller: ProfilerFiller
  ) {
    `object`.forEach { (resourceLocation: ResourceLocation?, jsonElement: JsonElement?) ->
      if (jsonElement!!.isJsonObject) {
        val jsonObject = jsonElement.asJsonObject
        if (jsonObject.has("disabled") && jsonObject["disabled"].asBoolean) {
          AlchimiaeMod.LOGGER.debug("Did not load {}; disabled", resourceLocation)
          return@forEach
        }
        if (jsonObject.has("required") && !Platform.isModLoaded(jsonObject["required"].asString)) {
          AlchimiaeMod.LOGGER.debug(
            "Did not load {}; missing required mod {}",
            resourceLocation,
            jsonObject["required"].asString
          )
          return@forEach
        }
      }
      loadResource(resourceLocation!!, jsonElement)
    }
  }

  companion object {
    var GSON: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
  }
}
