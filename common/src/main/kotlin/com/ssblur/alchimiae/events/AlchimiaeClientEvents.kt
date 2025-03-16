package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.unfocused.event.client.ClientDisconnectEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object AlchimiaeClientEvents {
  fun register() {
    ClientDisconnectEvent.register{
      ClientAlchemyHelper.reset()
    }
  }
}