package com.ssblur.alchimiae.effect

import com.ssblur.unfocused.extension.MinecraftServerExtension.runOnce
import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import kotlin.math.pow

class ExtendMobEffect(val modifier: Double) :
  InstantenousMobEffect(if(modifier < 1) MobEffectCategory.HARMFUL else MobEffectCategory.BENEFICIAL, -0x10000) {
  override fun onEffectAdded(livingEntity: LivingEntity, i: Int) {
    livingEntity.server?.runOnce {
      val effects = livingEntity.activeEffects.toList()
      effects.forEach { effect ->
        if(effect.effect.value()?.isInstantenous != true) {
          livingEntity.removeEffect(effect.effect)
          livingEntity.addEffect(
            MobEffectInstance(
              effect.effect,
              (effect.duration * modifier.pow(i + 1)).coerceAtMost(Int.MAX_VALUE.toDouble()).toInt(),
              effect.amplifier
            )
          )
        } else if(effect.effect.value() == this) {
          livingEntity.removeEffect(effect.effect)
        }
      }
    }
    super.onEffectAdded(livingEntity, i)
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int) = true

  override fun isInstantenous() = true
}
