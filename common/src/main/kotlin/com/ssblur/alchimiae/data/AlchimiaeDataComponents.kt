package com.ssblur.alchimiae.data

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.serialization.KClassCodec

object AlchimiaeDataComponents {
  val CUSTOM_POTION = AlchimiaeMod.registerDataComponent("custom_potion") {
    it.persistent(KClassCodec.codec(CustomPotionEffects::class))
      .networkSynchronized(KClassCodec.streamCodec(CustomPotionEffects::class))
      .build()
  }

  val POTION_RECIPE = AlchimiaeMod.registerDataComponent("potion_recipe") {
    it.persistent(KClassCodec.codec(PotionRecipe::class))
      .networkSynchronized(KClassCodec.streamCodec(PotionRecipe::class))
      .build()
  }

  val PLAYER_DATA = AlchimiaeMod.registerDataComponent("player_data") {
    it.persistent(KClassCodec.codec(PlayerData::class))
      .networkSynchronized(KClassCodec.streamCodec(PlayerData::class))
      .build()
  }

  fun register() {}
}