package com.ssblur.alchimiae.alchemy

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ssblur.alchimiae.AlchimiaeMod
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ExtraCodecs

data class AlchemyIngredient(val duration: Int, val effects: List<IngredientEffect>) {
  fun effectKeys(): List<ResourceLocation> {
    return effects.stream().map { effect -> effect?.effect ?: AlchimiaeMod.location("error") }.toList()
  }

  companion object {
    val CODEC: Codec<AlchemyIngredient> =
      RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<AlchemyIngredient> ->
        instance.group<Int, List<IngredientEffect>>(
          ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(AlchemyIngredient::duration),
          IngredientEffect.CODEC.listOf().fieldOf("effects").forGetter(AlchemyIngredient::effects)
        ).apply(
          instance
        ) { duration: Int, effects: List<IngredientEffect> ->
          AlchemyIngredient(
            duration,
            effects
          )
        }
      }
  }
}
