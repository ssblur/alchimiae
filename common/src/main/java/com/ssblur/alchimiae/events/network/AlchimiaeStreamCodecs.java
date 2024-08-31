package com.ssblur.alchimiae.events.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class AlchimiaeStreamCodecs {
  public static final StreamCodec<ByteBuf, Vec3> VEC3 = StreamCodec.composite(
    ByteBufCodecs.DOUBLE,
    Vec3::x,
    ByteBufCodecs.DOUBLE,
    Vec3::y,
    ByteBufCodecs.DOUBLE,
    Vec3::z,
    Vec3::new
  );

  public static <T extends Enum<T>> StreamCodec<FriendlyByteBuf, T> fromEnum(Class<T> c) {
    return StreamCodec.of(FriendlyByteBuf::writeEnum, e -> e.readEnum(c));
  }
}
