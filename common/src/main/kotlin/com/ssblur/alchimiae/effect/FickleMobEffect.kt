package com.ssblur.alchimiae.effect

import com.ssblur.alchimiae.resource.Effects
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import kotlin.jvm.optionals.getOrNull

class FickleMobEffect() :
  InstantenousMobEffect(MobEffectCategory.NEUTRAL, 0xffff00ffu.toInt()) {
  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    if(livingEntity.level().isClientSide) return true
    val effect = Effects.effects.values.filter { !(it.instant ?: false) }.random()
    if(BuiltInRegistries.MOB_EFFECT.containsKey(effect.effect)) {
      livingEntity.addEffect(MobEffectInstance(
        BuiltInRegistries.MOB_EFFECT.getHolder(effect.effect).getOrNull()!!,
        1200,
        i
      ))
    }
    livingEntity.removeEffect(AlchimiaeEffects.FICKLE.ref())
    return true
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int) = true

  override fun isInstantenous() = true
}
