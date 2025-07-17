package com.ssblur.alchimiae.effect

import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import kotlin.math.pow

class ExtendMobEffect(val modifier: Double) :
  InstantenousMobEffect(if(modifier < 1) MobEffectCategory.HARMFUL else MobEffectCategory.BENEFICIAL, -0x10000) {
  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    livingEntity.activeEffects.map { effect ->
      if(effect.effect.value() !is ExtendMobEffect) {
        livingEntity.removeEffect(effect.effect)
        livingEntity.addEffect(
          MobEffectInstance(
            effect.effect,
            (effect.duration * modifier.pow(i)).toInt(),
            effect.amplifier
          )
        )
      }
    }
    return true
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int) = true

  override fun isInstantenous() = true
}
