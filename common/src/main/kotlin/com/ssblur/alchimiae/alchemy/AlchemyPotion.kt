package com.ssblur.alchimiae.alchemy

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.level.Level

@JvmRecord
data class AlchemyPotion(val effects: Map<ResourceLocation, Int>, val duration: Int) {
  fun toPotion(level: Level): List<MobEffectInstance> {
    val list: MutableList<MobEffectInstance> = ArrayList()
    for ((key, value) in effects) {
      val effect = level.registryAccess().registry(Registries.MOB_EFFECT).get().getHolder(key)
      if (effect.isPresent)
        list.add(
          MobEffectInstance(
            effect.get(),
            if (effect.get().value().isInstantenous) 0 else duration,
            value
          )
        )
    }
    return list
  }
}
