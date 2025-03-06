package com.ssblur.alchimiae.effect

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class ScaleMobEffect(var scale: Double, var location: ResourceLocation) :
  MobEffect(MobEffectCategory.NEUTRAL, -0xaf78bd) {
  init {
    this.addAttributeModifier(
      Attributes.SCALE, location.withSuffix("_att_scale"),
      scale, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    )
    if (scale > 0) {
      this.addAttributeModifier(
        Attributes.MOVEMENT_SPEED,
        location.withSuffix("_att_movement_speed"),
        scale / 1.5,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.SNEAKING_SPEED,
        location.withSuffix("_att_sneaking_speed"),
        scale / 1.5,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.JUMP_STRENGTH,
        location.withSuffix("_att_jump_strength"),
        scale / 3.0,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.STEP_HEIGHT,
        location.withSuffix("_att_step_height"),
        scale / 1.5,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.WATER_MOVEMENT_EFFICIENCY,
        location.withSuffix("water_movement"),
        scale / 1.5,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.BLOCK_INTERACTION_RANGE,
        location.withSuffix("_att_block_range"),
        scale / 1.5,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.ENTITY_INTERACTION_RANGE,
        location.withSuffix("_att_range"),
        scale / 1.5,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.SAFE_FALL_DISTANCE,
        location.withSuffix("_att_fall_distance"),
        scale * 1.3,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
    } else {
      this.addAttributeModifier(
        Attributes.SAFE_FALL_DISTANCE,
        location.withSuffix("_att_fall_distance"),
        -scale,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.OXYGEN_BONUS,
        location.withSuffix("_att_oxygen"),
        -scale,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
      this.addAttributeModifier(
        Attributes.ENTITY_INTERACTION_RANGE, location.withSuffix("_att_range"),
        scale, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
      )
    }
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
    return false
  }
}
