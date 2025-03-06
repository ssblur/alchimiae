package com.ssblur.alchimiae.resource

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader

object IngredientClasses {
  data class ClassResource(val rarity: Float, val guaranteedEffects: Array<String>)
  val groups = AlchimiaeMod.registerSimpleDataLoader("alchimiae/groups", ClassResource::class)
}