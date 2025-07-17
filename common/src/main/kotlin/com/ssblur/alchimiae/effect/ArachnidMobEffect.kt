package com.ssblur.alchimiae.effect

import net.minecraft.core.Direction
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

class ArachnidMobEffect() :
  MobEffect(MobEffectCategory.BENEFICIAL, 0xffff0000u.toInt()) {
  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
    return false
  }

  companion object {
    fun isAffected(entity: LivingEntity): Boolean {
      if(entity.activeEffects.none{
        it.effect.value() is ArachnidMobEffect
      }) return false
      val pos = entity.blockPosition()
      val level: Level = entity.level()
      return (
        level.getBlockState(pos.east()).isFaceSturdy(level, pos.east(), Direction.WEST) ||
        level.getBlockState(pos.west()).isFaceSturdy(level, pos.west(), Direction.EAST) ||
        level.getBlockState(pos.north()).isFaceSturdy(level, pos.north(), Direction.SOUTH) ||
        level.getBlockState(pos.south()).isFaceSturdy(level, pos.south(), Direction.NORTH)
      )
    }
  }
}
