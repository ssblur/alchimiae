package com.ssblur.alchimiae.events;

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper;
import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.player.LocalPlayer;

public class ClientQuitEvent implements ClientPlayerEvent.ClientPlayerQuit {
  @Override
  public void quit(LocalPlayer player) {
    ClientAlchemyHelper.reset();
  }
}
