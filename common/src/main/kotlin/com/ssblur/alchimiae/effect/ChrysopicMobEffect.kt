package com.ssblur.alchimiae.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

class ChrysopicMobEffect() :
  MobEffect(MobEffectCategory.BENEFICIAL, 0xff5533aau.toInt()) {
  override fun shouldApplyEffectTickThisTick(i: Int, j: Int) = true

  override fun applyEffectTick(livingEntity: LivingEntity, i: Int): Boolean {
    val modifier = i + if(livingEntity.getItemBySlot(EquipmentSlot.CHEST).`is`(Items.ELYTRA)) 3 else 0
    if(livingEntity.isFallFlying)
      livingEntity.deltaMovement = livingEntity.deltaMovement.scale(1.0 + (0.0045 * modifier - 0.009))
    return super.applyEffectTick(livingEntity, i)
  }

  companion object {
    fun isAffected(entity: Player) = entity.activeEffects.any {
      it.effect.value() is ChrysopicMobEffect
    }
  }
}
