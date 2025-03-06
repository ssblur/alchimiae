package com.ssblur.alchimiae.network.client

import com.ssblur.alchimiae.AlchimiaeMod.location
import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

object AlchimiaeNetworkS2C {
  data class SendIngredients(val key: String, val effects: List<String>)
  val sendIngredients = NetworkManager.registerS2C(
    location("client_ingredients"),
    SendIngredients::class
  ) { (key, effects) ->
    ClientAlchemyHelper.update(key, effects)
  }

  enum class ParticleType { FLAME, SMOKE }
  data class SendParticle(val pos: Vec3, val particleType: ParticleType)
  val particle = NetworkManager.registerS2C(
    location("client_particles"),
    SendParticle::class
  ) { (pos, particleType) ->
    val x = pos.x
    val y = pos.y
    val z = pos.z
    val level = Minecraft.getInstance().level
    if (level != null)
      when (particleType) {
        ParticleType.FLAME -> level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.001, 0.0)
        ParticleType.SMOKE -> level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.1, 0.0)
      }
  }

  fun flameParticle(pos: Vec3, players: List<Player>) {
    particle(SendParticle(pos, ParticleType.FLAME), players)
  }

  fun smokeParticle(pos: Vec3, players: List<Player>) {
    particle(SendParticle(pos, ParticleType.SMOKE), players)
  }
}