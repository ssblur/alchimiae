package com.ssblur.alchimiae.block

import com.ssblur.alchimiae.AlchimiaeMod

object AlchimiaeBlocks {
  val BOILER = AlchimiaeMod.registerBlock("boiler") { BoilerBlock() }

  fun register() {}
}
