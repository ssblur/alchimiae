package com.ssblur.alchimiae.events.network.client;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper;
import com.ssblur.alchimiae.events.network.AlchimiaNetworkInterface;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public class ReceiveIngredientsNetwork implements AlchimiaNetworkInterface<ReceiveIngredientsNetwork.Payload> {
  @Override
  public CustomPacketPayload.Type<Payload> type() {
    return Payload.TYPE;
  }

  @Override
  public StreamCodec<RegistryFriendlyByteBuf, Payload> streamCodec() {
    return Payload.STREAM_CODEC;
  }

  @Override
  public NetworkManager.Side side() {
    return NetworkManager.Side.S2C;
  }

  @Override
  public void receive(Payload value, NetworkManager.PacketContext context) {
    AlchimiaeMod.LOGGER.debug("Received data for ingredient with key {}", value.key);
    ClientAlchemyHelper.update(value.key, value.effects);
  }

  public record Payload(String key, List<String> effects) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(AlchimiaeMod.location("receive_ingredients"));
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      Payload::key,
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      Payload::effects,
      Payload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }
}
