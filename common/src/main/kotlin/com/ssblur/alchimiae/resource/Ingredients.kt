package com.ssblur.alchimiae.resource

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader

object Ingredients {
  data class IngredientResource(
    val item: String,
    val guaranteedEffects: Array<String>,
    val ingredientClasses: Array<String>,
    val rarity: Float,
    val duration: Int,
    val noEffects: Boolean
  )

  val ingredients = AlchimiaeMod.registerSimpleDataLoader("alchimiae/ingredients", IngredientResource::class)
}