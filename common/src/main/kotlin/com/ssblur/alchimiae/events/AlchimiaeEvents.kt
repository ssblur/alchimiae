package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.data.IngredientEffectsSavedData
import com.ssblur.alchimiae.data.IngredientMemorySavedData
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.network.client.AlchimiaeNetworkS2C
import com.ssblur.alchimiae.network.server.AlchimiaeNetworkC2S
import com.ssblur.alchimiae.resource.CustomEffects
import com.ssblur.unfocused.event.client.ClientDisconnectEvent
import com.ssblur.unfocused.event.common.PlayerCraftEvent
import com.ssblur.unfocused.event.common.PlayerJoinedEvent
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer


object AlchimiaeEvents {
  fun register() {
    AlchimiaeNetworkS2C
    AlchimiaeNetworkC2S

    PlayerJoinedEvent.register{ player ->
      val data: IngredientMemorySavedData = IngredientMemorySavedData.computeIfAbsent(player)
      data.sync(player)

      AlchimiaeNetworkS2C.syncCustomEffects(
        AlchimiaeNetworkS2C.SyncCustomEffects(CustomEffects.customEffects), listOf(player)
      )
    }

    ClientDisconnectEvent.register{
      ClientAlchemyHelper.reset()
    }

    PlayerCraftEvent.register{ (player, constructed, inventory) ->
      if (player is ServerPlayer && constructed.`is`(AlchimiaeItems.MASH.get())) {
        val level = player.serverLevel()
        val effects = constructed[AlchimiaeDataComponents.CUSTOM_POTION]

        val memory: IngredientMemorySavedData = IngredientMemorySavedData.computeIfAbsent(player)
        val data: IngredientEffectsSavedData = IngredientEffectsSavedData.computeIfAbsent(level)

        effects?.let {
          for (i in 0..<inventory.containerSize) {
            val id = BuiltInRegistries.ITEM.getKey(inventory.getItem(i).item)
            data.data[id] ?: continue
            memory.add(player, inventory.getItem(i).item, it.effects.map { (key, _, _) -> key })
          }
        }
      }
    }
  }
}
