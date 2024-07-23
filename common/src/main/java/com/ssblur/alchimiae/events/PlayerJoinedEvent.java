package com.ssblur.alchimiae.events;

import com.ssblur.alchimiae.data.IngredientMemorySavedData;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;

public class PlayerJoinedEvent implements PlayerEvent.PlayerJoin {
  @Override
  public void join(ServerPlayer player) {
    var data = IngredientMemorySavedData.computeIfAbsent(player);
    data.sync(player);
  }
}
