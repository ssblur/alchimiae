package com.ssblur.alchimiae.events.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class AlchimiaeNetwork {
  public static void register() {

  }

  public static <T extends CustomPacketPayload> void register(AlchimiaNetworkInterface<T> networkInterface) {
    if(networkInterface.side() == NetworkManager.Side.C2S || Platform.getEnv() == EnvType.CLIENT)
      NetworkManager.registerReceiver(networkInterface.side(), networkInterface.type(), networkInterface.streamCodec(), networkInterface);

    if(networkInterface.side() == NetworkManager.Side.S2C && Platform.getEnv() == EnvType.SERVER)
      NetworkManager.registerS2CPayloadType(networkInterface.type(), networkInterface.streamCodec());
  }
}
