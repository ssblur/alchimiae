package com.ssblur.alchimiae.events.network

import io.netty.buffer.ByteBuf
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3

object AlchimiaeStreamCodecs {
  val VEC3: StreamCodec<ByteBuf?, Vec3> = StreamCodec.composite(
    ByteBufCodecs.DOUBLE,
    { obj: Vec3 -> obj.x() },
    ByteBufCodecs.DOUBLE,
    { obj: Vec3 -> obj.y() },
    ByteBufCodecs.DOUBLE,
    { obj: Vec3 -> obj.z() },
    { d, e, f -> Vec3(d, e, f) }
  )

  fun <T : Enum<T>?> fromEnum(c: Class<T>): StreamCodec<FriendlyByteBuf?, T> {
    return StreamCodec.of(
      { obj, enumerator-> obj?.writeEnum(enumerator) },
      { e -> e?.readEnum(c) })
  }
}
