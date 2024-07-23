package com.ssblur.alchimiae.events.network.client;

import com.ssblur.alchimiae.events.network.AlchimiaNetworkInterface;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ReceiveIngredientsNetwork implements AlchimiaNetworkInterface {
  @Override
  public CustomPacketPayload.Type type() {
    return null;
  }

  @Override
  public StreamCodec streamCodec() {
    return null;
  }

  @Override
  public NetworkManager.Side side() {
    return null;
  }

  @Override
  public void receive(Object value, NetworkManager.PacketContext context) {

  }
}
