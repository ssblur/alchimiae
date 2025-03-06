package com.ssblur.alchimiae.events.network.client;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.events.network.AlchimiaNetworkInterface;
import com.ssblur.alchimiae.events.network.AlchimiaeStreamCodecs;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

public class ParticleNetwork implements AlchimiaNetworkInterface<ParticleNetwork.Payload> {
  public enum TYPE {
    FLAME,  SMOKE
  }

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
    double x = value.pos.x;
    double y = value.pos.y;
    double z = value.pos.z;
    var level = Minecraft.getInstance().level;
    if(level != null)
      switch (value.particleType()) {
        case FLAME -> level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.001, 0.0);
        case SMOKE -> level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.1, 0.0);
      }
  }

  public record Payload(Vec3 pos, TYPE particleType) implements CustomPacketPayload {
    public static final Type<Payload> TYPE = new Type<>(AlchimiaeMod.location("particle"));
    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> STREAM_CODEC = StreamCodec.composite(
      AlchimiaeStreamCodecs.VEC3,
      Payload::pos,
      AlchimiaeStreamCodecs.fromEnum(TYPE.class),
      Payload::particleType,
      Payload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }
}
