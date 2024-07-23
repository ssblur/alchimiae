package com.ssblur.alchimiae.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record AlchemyIngredient(int duration, List<IngredientEffect> effects) {
  public static final Codec<AlchemyIngredient> CODEC = RecordCodecBuilder.create(instance ->
    instance.group(
      ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(AlchemyIngredient::duration),
      IngredientEffect.CODEC.listOf().fieldOf("effects").forGetter(AlchemyIngredient::effects)
    ).apply(instance, AlchemyIngredient::new)
  );
}
