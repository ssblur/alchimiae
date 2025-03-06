package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.events.network.AlchimiaeNetwork
import com.ssblur.alchimiae.events.reloadlisteners.EffectReloadListener
import com.ssblur.alchimiae.events.reloadlisteners.IngredientGroupReloadListener
import com.ssblur.alchimiae.events.reloadlisteners.IngredientReloadListener
import dev.architectury.event.events.client.ClientPlayerEvent
import dev.architectury.event.events.client.ClientTooltipEvent
import dev.architectury.event.events.common.PlayerEvent
import dev.architectury.platform.Platform
import dev.architectury.registry.ReloadListenerRegistry
import net.fabricmc.api.EnvType
import net.minecraft.server.packs.PackType

object AlchimiaeEvents {
  fun register() {
    ReloadListenerRegistry.register(PackType.SERVER_DATA, EffectReloadListener.Companion.INSTANCE)
    ReloadListenerRegistry.register(PackType.SERVER_DATA, IngredientReloadListener.Companion.INSTANCE)
    ReloadListenerRegistry.register(PackType.SERVER_DATA, IngredientGroupReloadListener.Companion.INSTANCE)
    PlayerEvent.CRAFT_ITEM.register(PlayerCraftedMashEvent())
    PlayerEvent.PLAYER_JOIN.register(PlayerJoinedEvent())

    if (Platform.getEnv() == EnvType.CLIENT) {
      ClientTooltipEvent.ITEM.register(AddTooltipEvent())
      ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(ClientQuitEvent())
    }

    AlchimiaeNetwork.register()
  }
}
