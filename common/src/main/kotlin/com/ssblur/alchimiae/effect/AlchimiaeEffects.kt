package com.ssblur.alchimiae.effect

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

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
  val CLEANSE = AlchimiaeMod.registerEffect("cleanse") { CleanseMobEffect(MobEffectCategory.HARMFUL) }
  val MUCK = AlchimiaeMod.registerEffect("muck") {
    CleanseMobEffect(MobEffectCategory.HARMFUL, MobEffectCategory.NEUTRAL)
  }
  val EXTEND = AlchimiaeMod.registerEffect("extend") {
    ExtendMobEffect(1.3333)
  }
  val DIMINISH = AlchimiaeMod.registerEffect("diminish") {
    ExtendMobEffect(0.6666)
  }
  val LEYDEN = AlchimiaeMod.registerEffect("leyden") {
    WeightMobEffect(0.5, AlchimiaeMod.location("leyden"))
  }

  val CUSTOM_EFFECT_BENEFICIAL = AlchimiaeMod.registerEffect("custom_effect_beneficial") {
    CustomMobEffect(MobEffectCategory.BENEFICIAL, 0x00ff00)
  }
  val CUSTOM_EFFECT_HARMFUL = AlchimiaeMod.registerEffect("custom_effect_harmful") {
    CustomMobEffect(MobEffectCategory.HARMFUL, 0xff0000)
  }
  val CUSTOM_EFFECT_NEUTRAL = AlchimiaeMod.registerEffect("custom_effect_neutral") {
    CustomMobEffect(MobEffectCategory.NEUTRAL, 0x0000ff)
  }
  val MISSING = AlchimiaeMod.registerEffect("missing") {
    CustomMobEffect(MobEffectCategory.NEUTRAL, 0x0000ff)
  }


  fun register() {}

  fun get(effect: RegistrySupplier<MobEffect>): Holder<MobEffect> {
    return effect.ref()
  }
}
