package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import dev.architectury.event.events.client.ClientPlayerEvent
import net.minecraft.client.player.LocalPlayer

class ClientQuitEvent : ClientPlayerEvent.ClientPlayerQuit {
  override fun quit(player: LocalPlayer?) {
    ClientAlchemyHelper.reset()
  }
}
