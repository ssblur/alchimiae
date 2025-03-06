package com.ssblur.alchimiae.effect

import com.ssblur.alchimiae.AlchimiaeMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

@Suppress("unused")
object AlchimiaeEffects {
  const val MOD_ID: String = AlchimiaeMod.MOD_ID
  val EFFECTS: DeferredRegister<MobEffect> = DeferredRegister.create(
    MOD_ID, Registries.MOB_EFFECT
  )

  val FAMINE: RegistrySupplier<MobEffect> = EFFECTS.register(
    "famine"
  ) { FoodMobEffect(false) }
  val FEAST: RegistrySupplier<MobEffect> = EFFECTS.register(
    "feast"
  ) { FoodMobEffect(true) }
  val IMMUNE: RegistrySupplier<MobEffect> = EFFECTS.register(
    "immune"
  ) { EffectModMobEffect(true) }
  val AFFLICTED: RegistrySupplier<MobEffect> = EFFECTS.register(
    "afflicted"
  ) { EffectModMobEffect(false) }
  val STRIDE: RegistrySupplier<MobEffect> = EFFECTS.register(
    "stride"
  ) { StrideMobEffect(AlchimiaeMod.location("stride")) }
  val FUSE: RegistrySupplier<MobEffect> = EFFECTS.register(
    "fuse"
  ) { FuseMobEffect() }
  val LILIPUTIAN: RegistrySupplier<MobEffect> = EFFECTS.register(
    "liliputian"
  ) { ScaleMobEffect(-0.25, AlchimiaeMod.location("liliputian")) }
  val ALICIAN: RegistrySupplier<MobEffect> = EFFECTS.register(
    "alician"
  ) { ScaleMobEffect(1.0, AlchimiaeMod.location("alician")) }

  fun register() {
    EFFECTS.register()
  }

  fun get(effect: RegistrySupplier<MobEffect?>): Holder<MobEffect>? {
    return EFFECTS.registrar.getHolder(effect.id)
  }

  fun get(effect: ResourceLocation?): Holder<MobEffect>? {
    return EFFECTS.registrar.getHolder(effect)
  }
}
