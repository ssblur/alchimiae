package com.ssblur.alchimiae.effect

import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class CleanseMobEffect(vararg val categoryToRemove: MobEffectCategory): InstantenousMobEffect(MobEffectCategory.BENEFICIAL, 10) {
  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    livingEntity.activeEffects.filter {
      categoryToRemove.contains(it.effect.value().category)
    }.map{
      it.effect
    }.forEach{
      livingEntity.removeEffect(it)
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
