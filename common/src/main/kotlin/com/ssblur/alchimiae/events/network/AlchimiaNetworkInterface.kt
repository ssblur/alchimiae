package com.ssblur.alchimiae.events.network

import dev.architectury.networking.NetworkManager
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface AlchimiaNetworkInterface<T : CustomPacketPayload?> :
  NetworkManager.NetworkReceiver<T> {
  fun type(): CustomPacketPayload.Type<T>
  fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf?, T>
  fun side(): NetworkManager.Side
}
