package com.ssblur.alchimiae.data

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.serialization.KClassCodec

object AlchimiaeDataComponents {
  val CUSTOM_POTION = AlchimiaeMod.registerDataComponent("custom_potion") {
    it.persistent(KClassCodec.codec(CustomPotionEffects::class))
      .networkSynchronized(KClassCodec.streamCodec(CustomPotionEffects::class))
      .build()
  }

  fun register() {}
}