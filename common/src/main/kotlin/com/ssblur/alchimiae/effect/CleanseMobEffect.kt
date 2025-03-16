package com.ssblur.alchimiae.effect

import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class CleanseMobEffect: InstantenousMobEffect(MobEffectCategory.BENEFICIAL, 10) {
  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    for(effect in livingEntity.activeEffects) {
      if(!effect.effect.value().isBeneficial) livingEntity.removeEffect(effect.effect)
    }
    return true
  }

  override fun applyInstantenousEffect(
    entity: Entity?,
    entity2: Entity?,
    livingEntity: LivingEntity,
    i: Int,
    d: Double
  ) {
    applyEffectTick(livingEntity, i)
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean = true
}
