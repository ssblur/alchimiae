package com.ssblur.alchimiae.alchemy

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.data.CustomEffect
import com.ssblur.alchimiae.data.CustomPotionEffects
import com.ssblur.alchimiae.resource.CustomEffects
import com.ssblur.alchimiae.resource.Effects
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity

@JvmRecord
data class AlchemyPotion(val effects: Map<ResourceLocation, Int>, val duration: Int) {
  fun apply(entity: LivingEntity) {
    for((key, value) in effects) {
      val effect = Effects.effects[key]
      if(effect == null) {
        AlchimiaeMod.LOGGER.warn("No effect was registered with key $key, yet an ingredient uses it")
        continue
      }
      val ticks = ((effect.durationMultiplier ?: 1.0f) * duration).toInt()

      if(BuiltInRegistries.MOB_EFFECT.containsKey(effect.effect)) {
        val mobEffect = BuiltInRegistries.MOB_EFFECT.getHolder(effect.effect).get()
        entity.addEffect(MobEffectInstance(mobEffect, duration, value))
      } else if(CustomEffects.customEffects.containsKey(effect.effect)) {
        CustomEffects.applyEffect(key, entity, ticks)
      } else {
        AlchimiaeMod.LOGGER.warn("An effect called ${effect.effect} was referenced by $key, but no mob effect" +
            " or custom effect exists with this id!")
      }
    }
  }

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

  fun toCustomPotion(): CustomPotionEffects {
    val list: MutableList<CustomEffect> = mutableListOf()
    for ((key, value) in effects) {
      val effectData = Effects.effects[key]
      if(effectData == null) {
        AlchimiaeMod.LOGGER.warn("Attempted to convert item with effect $key, which does not exist.")
        AlchimiaeMod.LOGGER.warn("(You cannot use mob effects without a corresponding Alchimiae effect entry)")
        AlchimiaeMod.LOGGER.warn("Does your data pack define it?")
        continue
      }
      list.add(CustomEffect(
        effectData.effect,
        if(effectData.instant == true) 1 else (duration * (effectData.durationMultiplier ?: 1.0f)).toInt(),
        value
      ))
    }
    return CustomPotionEffects(list)
  }
}
