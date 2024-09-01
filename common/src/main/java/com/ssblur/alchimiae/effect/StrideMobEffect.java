package com.ssblur.alchimiae.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class StrideMobEffect extends MobEffect {
  ResourceLocation location;
  public StrideMobEffect(ResourceLocation attributeLocation) {
    super(MobEffectCategory.BENEFICIAL, 0xFF66FFEA);

    this.location = attributeLocation;
    this.addAttributeModifier(Attributes.STEP_HEIGHT, attributeLocation.withSuffix("step_height"), 1d, AttributeModifier.Operation.ADD_VALUE);
    this.addAttributeModifier(Attributes.MOVEMENT_SPEED, attributeLocation.withSuffix("movement_speed"), 0.33d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    this.addAttributeModifier(Attributes.SNEAKING_SPEED, attributeLocation.withSuffix("sneaking_speed"), 0.33d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int i, int j) {
    return false;
  }
}
