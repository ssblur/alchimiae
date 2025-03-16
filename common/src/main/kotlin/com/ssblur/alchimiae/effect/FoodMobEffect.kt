package com.ssblur.alchimiae.effect

import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class FoodMobEffect(refill: Boolean) :
  InstantenousMobEffect(if (refill) MobEffectCategory.BENEFICIAL else MobEffectCategory.HARMFUL, 10) {
  var signum: Int = if (refill) 1 else -1

  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    if (livingEntity is Player) livingEntity.foodData.eat((i + 1) * signum * 3, 0f)
    return true
  }

  override fun applyInstantenousEffect(
    entity: Entity?,
    entity2: Entity?,
    livingEntity: LivingEntity,
    i: Int,
    d: Double
  ) {
    if (livingEntity is Player) livingEntity.foodData.eat((i + 1) * signum * 3, 0f)
  }
}
