package com.ssblur.alchimiae.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity

class EffectModMobEffect(var removeNegative: Boolean) :
  MobEffect(if (removeNegative) MobEffectCategory.BENEFICIAL else MobEffectCategory.HARMFUL, -0x314cff01) {
  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    livingEntity.activeEffects.forEach{ instance: MobEffectInstance ->
      val effect = instance.effect.value()
      if (effect is EffectModMobEffect) return@forEach
      if (effect.category != MobEffectCategory.NEUTRAL && removeNegative != effect.isBeneficial) {
        if (instance.duration == -1) return@forEach
        val duration = instance.duration - 5 * (i + 1)
        if (duration > 0) livingEntity.forceAddEffect(
          MobEffectInstance(
            instance.effect,
            duration,
            instance.amplifier,
            instance.isAmbient,
            instance.isVisible,
            instance.showIcon(),
            null
          ),
          null
        )
        else livingEntity.removeEffect(instance.effect)
      }
    }
    return true
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
    return (i % 5) == 0
  }
}
