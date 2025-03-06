package com.ssblur.alchimiae.events.reloadlisteners

import com.google.common.reflect.TypeToken
import com.google.gson.JsonElement
import com.ssblur.alchimiae.AlchimiaeMod
import net.minecraft.resources.ResourceLocation
import java.lang.reflect.Type

class IngredientGroupReloadListener : AlchimiaeReloadListener("alchimiae/groups") {
  @JvmRecord
  data class ClassResource(val rarity: Float, val guaranteedEffects: Array<String>)

  var groups: HashMap<ResourceLocation, ClassResource> = HashMap()

  override fun loadResource(resourceLocation: ResourceLocation, jsonElement: JsonElement?) {
    val resource: ClassResource =
      AlchimiaeReloadListener.Companion.GSON.fromJson<ClassResource>(jsonElement, EFFECT_TYPE)
    AlchimiaeMod.LOGGER.debug(
      "Loaded ingredient class {}",
      resourceLocation
    )
    groups[resourceLocation] = resource
  }

  companion object {
    var EFFECT_TYPE: Type = object : TypeToken<ClassResource?>() {}.type
    val INSTANCE: IngredientGroupReloadListener = IngredientGroupReloadListener()
  }
}
