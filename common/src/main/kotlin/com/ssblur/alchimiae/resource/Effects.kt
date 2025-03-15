package com.ssblur.alchimiae.resource

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.resources.ResourceLocation

object Effects {
  data class EffectResource(
    val effect: ResourceLocation, // The location of the mob effect or custom effect to give
    val rarity: Float?, // If defined, the rarity of this effect. If not, this won't be randomly distributed
    val durationMultiplier: Float?, // A scalar to apply to duration
    val instant: Boolean? // Whether this effect is instant
  )
  val effects = AlchimiaeMod.registerSimpleDataLoader("alchimiae/effects", EffectResource::class)
}