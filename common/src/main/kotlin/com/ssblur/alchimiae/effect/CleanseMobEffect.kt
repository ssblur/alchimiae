package com.ssblur.alchimiae.effect

import com.ssblur.unfocused.extension.MinecraftServerExtension.runOnce
import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectCategory.BENEFICIAL
import net.minecraft.world.effect.MobEffectCategory.HARMFUL
import net.minecraft.world.entity.LivingEntity

class CleanseMobEffect(vararg val categoryToRemove: MobEffectCategory):
  InstantenousMobEffect(if(categoryToRemove.contains(HARMFUL)) BENEFICIAL else HARMFUL, 10) {
  override fun onEffectAdded(livingEntity: LivingEntity, i: Int) {
    livingEntity.server?.runOnce {
      (0..i).forEach { _ ->
        livingEntity.activeEffects.filter {
          categoryToRemove.contains(it.effect.value().category)
        }.map {
          it.effect
        }.firstOrNull()?.let {
          livingEntity.removeEffect(it)
        }
      }
    }
    super.onEffectAdded(livingEntity, i)
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean = true
}
