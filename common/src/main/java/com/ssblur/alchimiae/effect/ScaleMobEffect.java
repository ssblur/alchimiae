package com.ssblur.alchimiae.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ScaleMobEffect extends MobEffect {
  double scale;
  ResourceLocation location;
  public ScaleMobEffect(double scale, ResourceLocation attributeLocation) {
    super(MobEffectCategory.NEUTRAL, 0xFF508743);

    this.scale = scale;
    this.location = attributeLocation;
    this.addAttributeModifier(Attributes.SCALE, attributeLocation, scale, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int i, int j) {
    return false;
  }
}
