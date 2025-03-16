package com.ssblur.alchimiae.effect

import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

class FuseMobEffect :
  InstantenousMobEffect(MobEffectCategory.HARMFUL, -0x10000) {
  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    val level = livingEntity.level()
    val pos = livingEntity.position()
    level.explode(null, pos.x, pos.y, pos.z, i + 2f, Level.ExplosionInteraction.NONE)
    return true
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
    return i == 1
  }
}
