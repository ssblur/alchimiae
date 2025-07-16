package com.ssblur.alchimiae.effect

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.Vec3

class WeightMobEffect(val modifier: Double, val location: ResourceLocation) :
  MobEffect(if(modifier > 0) MobEffectCategory.HARMFUL else MobEffectCategory.BENEFICIAL, -0xaf78bd) {
  init {
    this.addAttributeModifier(
      Attributes.GRAVITY, location.withSuffix("_att_gravity"),
      modifier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    )
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int) = true

  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    if(livingEntity.isInWater) {
      livingEntity.addDeltaMovement(Vec3(0.0, modifier / -5, 0.0))
    }
    return super.applyEffectTick(livingEntity, i)
  }
}
