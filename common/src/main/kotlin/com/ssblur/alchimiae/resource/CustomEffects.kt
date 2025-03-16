package com.ssblur.alchimiae.resource

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.effect.AlchimiaeEffects
import com.ssblur.alchimiae.network.client.AlchimiaeNetworkS2C
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

object CustomEffects {
  data class CustomEffect(
    val category: MobEffectCategory,
    val tickCommands: List<String>?,
    val tickFrequency: Int?,
    val applyCommands: List<String>?,
    val icon: String?,
    val name: String?,
    val color: String?,
    var location: ResourceLocation?,
  )

  var customEffects = mutableMapOf<ResourceLocation, CustomEffect>()
  init {
    AlchimiaeMod.registerDataLoader(
      "alchimiae/custom_effects",
      CustomEffect::class
    ) { effect, location ->
      effect.location = location
      customEffects[location] = effect
      if(BuiltInRegistries.MOB_EFFECT.containsKey(location)) {
        AlchimiaeMod.LOGGER.warn("Custom Effect with id $location conflicts with an existing mob effect!")
        AlchimiaeMod.LOGGER.warn("Potions which use this effect will always default to the mob effect!")
      }
    }
  }

  fun applyEffect(location: ResourceLocation, entity: LivingEntity, ticks: Int = 600) {
    customEffects[location]?.let {
      if(ticks <= 1) {
        AlchimiaeEffects.CUSTOM_EFFECT_HARMFUL.get().applyEffectTick(entity, 0)
      } else {
        entity.setCustomEffect(it, entity.level().gameTime + ticks)
        entity.addEffect(MobEffectInstance(getMobEffectFor(it), ticks))
      }
    }
  }

  private fun getMobEffectFor(customEffect: CustomEffect): Holder.Reference<MobEffect> {
    return BuiltInRegistries.MOB_EFFECT.getHolder(when(customEffect.category) {
      MobEffectCategory.BENEFICIAL -> AlchimiaeEffects.CUSTOM_EFFECT_BENEFICIAL.key
      MobEffectCategory.HARMFUL -> AlchimiaeEffects.CUSTOM_EFFECT_HARMFUL.key
      else -> AlchimiaeEffects.CUSTOM_EFFECT_NEUTRAL.key
    }!!).get()
  }

  private val entityCustomEffectData: MutableMap<LivingEntity, MutableMap<CustomEffect, Long>> = mutableMapOf()

  var LivingEntity.customEffects: MutableMap<CustomEffect, Long>
    get() {
      if(entityCustomEffectData[this] == null) entityCustomEffectData[this] = mutableMapOf()
      return entityCustomEffectData[this]!!
    }
    set(effects) {
      if(this is ServerPlayer) {
        entityCustomEffectData[this]?.mapKeys { (key, _) -> key.location!! }?.let {
          AlchimiaeNetworkS2C.sendCustomEffects(AlchimiaeNetworkS2C.SendCustomEffects(it), listOf(this))
        }
      }
      entityCustomEffectData[this] = effects
    }

  @Suppress("unused")
  fun LivingEntity.getCustomEffect(customEffect: CustomEffect) = this.customEffects[customEffect] ?: 0
  fun LivingEntity.setCustomEffect(customEffect: CustomEffect, time: Long?) {
    if(time == null) this.customEffects.remove(customEffect)
    else this.customEffects[customEffect] = time
    if(this is Player) {
      entityCustomEffectData[this]?.mapKeys { (key, _) -> key.location!! }?.let {
        AlchimiaeNetworkS2C.sendCustomEffects(AlchimiaeNetworkS2C.SendCustomEffects(it), listOf(this))
      }
    }
  }

  fun readFromCompoundTag(livingEntity: LivingEntity, compoundTag: CompoundTag) {
    val tag = compoundTag.getCompound("alchimiae:custom_effects")
    val effects = livingEntity.customEffects
    for((key, value) in customEffects)
      if(tag.contains(key.toString()))
        effects[value] = tag.getLong(key.toString())
  }

  fun writeToCompoundTag(livingEntity: LivingEntity, compoundTag: CompoundTag) {
    val tag = CompoundTag()
    for((key, value) in livingEntity.customEffects) {
      tag.putLong(key.location.toString(), value)
    }
    compoundTag.put("alchimiae:custom_effects", tag)
  }

  fun remove(livingEntity: LivingEntity) {
    entityCustomEffectData.remove(livingEntity)
  }
}