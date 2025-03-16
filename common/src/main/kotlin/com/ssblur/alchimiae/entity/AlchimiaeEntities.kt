package com.ssblur.alchimiae.entity

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.rendering.EntityRendering.registerEntityRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object AlchimiaeEntities {
  val CUSTOM_EFFECT_CLOUD = AlchimiaeMod.registerEntity("custom_effect_cloud") {
    EntityType.Builder.of(
      { type, level -> CustomEffectCloud(type, level) },
      MobCategory.MISC
    )
      .clientTrackingRange(32)
      .sized(6.0f, 0.5f)
      .build("custom_effect_cloud")
  }

  fun register() {
    try { clientRegister() } catch (_: NoSuchMethodError) {}
  }

  @Environment(EnvType.CLIENT)
  fun clientRegister() {
    AlchimiaeMod.registerEntityRenderer(CUSTOM_EFFECT_CLOUD) { NoopRenderer(it) }
  }
}