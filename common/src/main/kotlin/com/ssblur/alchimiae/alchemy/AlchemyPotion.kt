package com.ssblur.alchimiae.alchemy

import com.ssblur.alchimiae.effect.AlchimiaeEffects
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance

@JvmRecord
data class AlchemyPotion(val effects: Map<ResourceLocation, Int>, val duration: Int) {
  fun toPotion(): List<MobEffectInstance> {
    val list: MutableList<MobEffectInstance> = ArrayList()
    for ((key, value) in effects) {
      val effect = AlchimiaeEffects.get(key)
      if (effect != null) list.add(
        MobEffectInstance(
          effect,
          if (effect.value().isInstantenous) 0 else duration,
          value
        )
      )
    }
    return list
  }
}
