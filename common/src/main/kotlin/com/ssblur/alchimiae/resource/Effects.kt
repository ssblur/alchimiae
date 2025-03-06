package com.ssblur.alchimiae.resource

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader

object Effects {
  data class EffectResource(val effect: String, val rarity: Float)
  val effects = AlchimiaeMod.registerSimpleDataLoader("alchimiae/effects", EffectResource::class)
}