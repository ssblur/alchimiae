package com.ssblur.alchimiae.events.reloadlisteners

import com.google.common.reflect.TypeToken
import com.google.gson.JsonElement
import com.ssblur.alchimiae.AlchimiaeMod
import net.minecraft.resources.ResourceLocation
import java.lang.reflect.Type

class EffectReloadListener : AlchimiaeReloadListener("alchimiae/effects") {
  @JvmRecord
  data class EffectResource(val effect: String, val rarity: Float)

  var effects: HashMap<ResourceLocation, EffectResource> = HashMap()

  override fun loadResource(resourceLocation: ResourceLocation, jsonElement: JsonElement?) {
    val resource: EffectResource =
      AlchimiaeReloadListener.Companion.GSON.fromJson<EffectResource>(jsonElement, EFFECT_TYPE)
    AlchimiaeMod.LOGGER.debug(
      "Loaded effect {} with potion effect {} and rarity {}",
      resourceLocation,
      resource.effect,
      resource.rarity
    )
    effects[resourceLocation] = resource
  }

  companion object {
    var EFFECT_TYPE: Type = object : TypeToken<EffectResource?>() {}.type
    val INSTANCE: EffectReloadListener = EffectReloadListener()
  }
}
