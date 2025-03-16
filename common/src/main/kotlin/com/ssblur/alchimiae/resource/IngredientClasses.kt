package com.ssblur.alchimiae.resource

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.resources.ResourceLocation

object IngredientClasses {
  data class ClassResource(val rarity: Float, val guaranteedEffects: List<ResourceLocation>)
  val groups = AlchimiaeMod.registerSimpleDataLoader("alchimiae/groups", ClassResource::class)
}