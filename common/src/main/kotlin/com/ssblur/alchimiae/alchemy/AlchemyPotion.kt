package com.ssblur.alchimiae.alchemy

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance

@JvmRecord
data class AlchemyPotion(val effects: Map<ResourceLocation, Int>, val duration: Int) {
  fun toPotion(): List<MobEffectInstance> {
    val list: MutableList<MobEffectInstance> = ArrayList()
    for ((key, value) in effects) {
      val effect = BuiltInRegistries.MOB_EFFECT.getHolder(key)
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
