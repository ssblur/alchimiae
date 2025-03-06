package com.ssblur.alchimiae.events.network.client

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.events.network.AlchimiaNetworkInterface
import com.ssblur.alchimiae.events.network.AlchimiaeStreamCodecs
import dev.architectury.networking.NetworkManager
import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.phys.Vec3

class ParticleNetwork : AlchimiaNetworkInterface<ParticleNetwork.Payload> {
  enum class TYPE { FLAME, SMOKE }

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
    val x = value.pos!!.x
    val y = value.pos.y
    val z = value.pos.z
    val level = Minecraft.getInstance().level
    if (level != null)
      when (value.particleType) {
        TYPE.FLAME -> level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.001, 0.0)
        TYPE.SMOKE -> level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.1, 0.0)
        null -> {}
      }
  }

  @JvmRecord
  data class Payload(val pos: Vec3?, val particleType: TYPE?) :
    CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
      return TYPE
    }

    companion object {
      val TYPE: CustomPacketPayload.Type<Payload> = CustomPacketPayload.Type(AlchimiaeMod.location("particle"))
      val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf?, Payload> = StreamCodec.composite(
        AlchimiaeStreamCodecs.VEC3,
        Payload::pos,
        AlchimiaeStreamCodecs.fromEnum(ParticleNetwork.TYPE::class.java),
        Payload::particleType
      ) { pos: Vec3?, particleType -> Payload(pos, particleType) }
    }
  }
}
