package com.ssblur.alchimiae.alchemy

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ExtraCodecs

@JvmRecord
data class IngredientEffect(val effect: ResourceLocation, val potency: Float) {
  companion object {
    val CODEC: Codec<IngredientEffect> =
      RecordCodecBuilder.create { instance ->
        instance.group(
          ResourceLocation.CODEC.fieldOf("effect").forGetter(IngredientEffect::effect),
          ExtraCodecs.POSITIVE_FLOAT.fieldOf("potency").forGetter(IngredientEffect::potency)
        ).apply(
          instance
        ) { effect: ResourceLocation, potency: Float ->
          IngredientEffect(
            effect,
            potency
          )
        }
      }
  }
}
