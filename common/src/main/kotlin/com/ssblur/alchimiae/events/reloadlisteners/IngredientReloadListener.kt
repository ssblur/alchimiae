package com.ssblur.alchimiae.events.reloadlisteners

import com.google.common.reflect.TypeToken
import com.google.gson.JsonElement
import com.ssblur.alchimiae.AlchimiaeMod
import net.minecraft.resources.ResourceLocation
import java.lang.reflect.Type

class IngredientReloadListener : AlchimiaeReloadListener("alchimiae/ingredients") {
  @JvmRecord
  data class IngredientResource(
    val item: String,
    val guaranteedEffects: Array<String>,
    val ingredientClasses: Array<String>,
    val rarity: Float,
    val duration: Int,
    val noEffects: Boolean
  )

  var ingredients: HashMap<ResourceLocation?, IngredientResource> = HashMap()

  override fun loadResource(resourceLocation: ResourceLocation, jsonElement: JsonElement?) {
    val resource: IngredientResource =
      AlchimiaeReloadListener.Companion.GSON.fromJson<IngredientResource>(jsonElement, EFFECT_TYPE)
    if (resourceLocation.namespace == "alchimiae" && !resourceLocation.path.endsWith(
        resource.item.split(":".toRegex())
          .dropLastWhile { it.isEmpty() }.toTypedArray()[1]
      )
    ) AlchimiaeMod.LOGGER.warn(
      "Ingredient {} was loaded with item {}! This is probably not intentional",
      resourceLocation,
      resource.item
    )
    AlchimiaeMod.LOGGER.debug(
      "Loaded ingredient {} with:\n\tGuaranteed Effects: {}\n\tIngredient Classes: {}",
      resourceLocation,
      resource.guaranteedEffects,
      resource.ingredientClasses
    )
    ingredients[resourceLocation] = resource
  }

  companion object {
    var EFFECT_TYPE: Type = object : TypeToken<IngredientResource?>() {}.type
    val INSTANCE: IngredientReloadListener = IngredientReloadListener()
  }
}
