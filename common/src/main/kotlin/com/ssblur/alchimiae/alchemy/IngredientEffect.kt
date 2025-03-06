package com.ssblur.alchimiae.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public record IngredientEffect(ResourceLocation effect, float potency) {
  public static final Codec<IngredientEffect> CODEC = RecordCodecBuilder.create(instance ->
    instance.group(
      ResourceLocation.CODEC.fieldOf("effect").forGetter(IngredientEffect::effect),
      ExtraCodecs.POSITIVE_FLOAT.fieldOf("potency").forGetter(IngredientEffect::potency)
    ).apply(instance, IngredientEffect::new)
  );
}
