package com.ssblur.alchimiae.effect

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class StrideMobEffect(var location: ResourceLocation) :
  MobEffect(MobEffectCategory.BENEFICIAL, -0x990016) {
  init {
    this.addAttributeModifier(
      Attributes.STEP_HEIGHT,
      location.withSuffix("step_height"),
      1.0,
      AttributeModifier.Operation.ADD_VALUE
    )
    this.addAttributeModifier(
      Attributes.MOVEMENT_SPEED,
      location.withSuffix("movement_speed"),
      0.33,
      AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    )
    this.addAttributeModifier(
      Attributes.SNEAKING_SPEED,
      location.withSuffix("sneaking_speed"),
      0.33,
      AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    )
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
    return false
  }
}
