package com.ssblur.alchimiae.events;

import com.ssblur.alchimiae.events.reloadlisteners.EffectReloadListener;
import com.ssblur.alchimiae.events.reloadlisteners.IngredientClassReloadListener;
import com.ssblur.alchimiae.events.reloadlisteners.IngredientReloadListener;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.server.packs.PackType;

public class AlchimiaeEvents {
  public static void register() {
    ReloadListenerRegistry.register(PackType.SERVER_DATA, EffectReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, IngredientReloadListener.INSTANCE);
    ReloadListenerRegistry.register(PackType.SERVER_DATA, IngredientClassReloadListener.INSTANCE);

    if(Platform.getEnv() == EnvType.CLIENT) {
      ClientTooltipEvent.ITEM.register(new AddTooltipEvent());
    }
  }
}
