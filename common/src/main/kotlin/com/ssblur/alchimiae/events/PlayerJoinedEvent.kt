package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.data.IngredientMemorySavedData
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.server.level.ServerPlayer

class PlayerJoinedEvent : PlayerEvent.PlayerJoin {
  override fun join(player: ServerPlayer) {
    val data: IngredientMemorySavedData = IngredientMemorySavedData.Companion.computeIfAbsent(player)
    data.sync(player)
  }
}
