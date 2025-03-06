package com.ssblur.alchimiae.events.network.client

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.alchimiae.events.network.AlchimiaNetworkInterface
import dev.architectury.networking.NetworkManager
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ReceiveIngredientsNetwork : AlchimiaNetworkInterface<ReceiveIngredientsNetwork.Payload> {
  override fun type(): CustomPacketPayload.Type<Payload> {
    return Payload.TYPE
  }

  override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf?, Payload> {
    return Payload.STREAM_CODEC
  }

  override fun side(): NetworkManager.Side {
    return NetworkManager.Side.S2C
  }

  override fun receive(value: Payload, context: NetworkManager.PacketContext) {
    AlchimiaeMod.LOGGER.debug("Received data for ingredient with key {}", value.key)
    ClientAlchemyHelper.update(value.key, value.effects)
  }

  data class Payload(val key: String, val effects: List<String>) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
      return TYPE
    }

    companion object {
      val TYPE: CustomPacketPayload.Type<Payload> =
        CustomPacketPayload.Type(AlchimiaeMod.location("receive_ingredients"))
      val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf?, Payload> = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        Payload::key,
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
        Payload::effects
      ) { key: String, effects -> Payload(key, effects) }
    }
  }
}
