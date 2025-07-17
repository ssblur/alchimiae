package com.ssblur.alchimiae.effect

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class DireFallMobEffect(val location: ResourceLocation) :
  MobEffect(MobEffectCategory.HARMFUL, 0xff62d5c8u.toInt()) {
  init {
    this.addAttributeModifier(
      Attributes.SAFE_FALL_DISTANCE, location.withSuffix("_att_fall_distance"),
      -1.0, AttributeModifier.Operation.ADD_VALUE
    )
    this.addAttributeModifier(
      Attributes.FALL_DAMAGE_MULTIPLIER, location.withSuffix("_att_fall_damage"),
      1.0, AttributeModifier.Operation.ADD_VALUE
    )
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int) = false
}
