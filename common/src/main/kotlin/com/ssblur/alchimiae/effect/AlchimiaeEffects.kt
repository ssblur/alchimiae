package com.ssblur.alchimiae.effect

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect

@Suppress("unused")
object AlchimiaeEffects {
  const val MOD_ID: String = AlchimiaeMod.MOD_ID

  val FAMINE = AlchimiaeMod.registerEffect("famine") { FoodMobEffect(false) }
  val FEAST = AlchimiaeMod.registerEffect("feast") { FoodMobEffect(true) }
  val IMMUNE = AlchimiaeMod.registerEffect("immune") { EffectModMobEffect(true) }
  val AFFLICTED = AlchimiaeMod.registerEffect("afflicted") { EffectModMobEffect(false) }
  val STRIDE = AlchimiaeMod.registerEffect("stride") { StrideMobEffect(AlchimiaeMod.location("stride")) }
  val FUSE = AlchimiaeMod.registerEffect("fuse") { FuseMobEffect() }
  val LILIPUTIAN = AlchimiaeMod.registerEffect("liliputian") {
    ScaleMobEffect(-0.25, AlchimiaeMod.location("liliputian"))
  }
  val ALICIAN = AlchimiaeMod.registerEffect("alician") {
    ScaleMobEffect(1.0, AlchimiaeMod.location("alician"))
  }

  fun register() {}

  fun get(effect: RegistrySupplier<MobEffect>): Holder<MobEffect> {
    return effect.ref()
  }
}
