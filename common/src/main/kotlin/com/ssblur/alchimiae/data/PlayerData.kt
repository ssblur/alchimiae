package com.ssblur.alchimiae.data

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.*

data class PlayerData(val name: String, val uuid: String) {
  fun getPlayer(level: Level): Player? {
    return level.getPlayerByUUID(UUID.fromString(uuid))
  }
  companion object {
    fun ofPlayer(player: Player): PlayerData {
      return PlayerData(player.displayName?.string ?: "", player.uuid.toString())
    }
  }
}
