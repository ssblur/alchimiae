package com.ssblur.alchimiae.effect

import com.ssblur.alchimiae.resource.CustomEffects
import com.ssblur.alchimiae.resource.CustomEffects.customEffects
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec2

class CustomMobEffect(mobEffectCategory: MobEffectCategory, i: Int) :
  MobEffect(mobEffectCategory, i) {
  override fun applyEffectTick(entity: LivingEntity, i: Int): Boolean {
    val effects = entity.customEffects
    val tick = entity.level().gameTime
    if(effects.isEmpty()) {
      val key = BuiltInRegistries.MOB_EFFECT.getResourceKey(this)
      if(key.isPresent)
        entity.removeEffect(BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(key.get()))
      return true
    }

    val commands = entity.level().server?.commands ?: return true
    val x = entity.x
    val y = entity.y
    val z = entity.z
    val entitySelector = getTargetSelector(entity)
    val dimension = entity.level().dimension().location()
    val stack = CommandSourceStack(
      CommandSource.NULL,
      entity.position(),
      Vec2(0f, 0f),
      entity.level() as ServerLevel,
      4,
      "Magic",
      Component.translatable("command.alchimiae.magic_name"),
      entity.level().server!!,
      null
    )
    val prefix = "execute positioned $x $y $z in $dimension as $entitySelector run"
    for((effect, expiry) in effects.entries.filter{ (it.key.category) == this.category }) {
      val frequency = effect.tickFrequency ?: 100
      if(expiry < tick) {
        effects.remove(effect)
        continue
      }
      if(effect.tickCommands?.isNotEmpty() == true && tick % frequency == 0L)
        for(command in effect.tickCommands) {
          commands.performCommand(commands.dispatcher.parse("$prefix $command", stack), "$prefix $command")
        }
    }

    return super.applyEffectTick(entity, i)
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean = true

  @Environment(EnvType.CLIENT)
  override fun getDisplayName(): Component {
    val effect = getCurrentEffect()
    return Component.translatable(effect?.name ?: "effect.${effect?.location?.toShortLanguageKey()}")
  }

  @Environment(EnvType.CLIENT)
  fun getCurrentEffect(): CustomEffects.CustomEffect? {
    val effects = Minecraft.getInstance().player?.customEffects?.filter {
      it.key.category == this.category
    }
    if(effects == null || effects.isEmpty()) return null
    val time = Minecraft.getInstance().level?.gameTime ?: 0L
    val index = (time / 60L) % effects.size
    val effect = effects.keys.toList()[index.toInt()]
    return effect
  }

  companion object {
    fun getTargetSelector(entity: Entity): String {
      val uuid = entity.uuid
      val l = uuid.leastSignificantBits
      val m = uuid.mostSignificantBits
      return String.format(
        "@e[nbt={UUID:[I;%d,%d,%d,%d]}]",
        (m shr 32).toInt(),
        m.toInt(),
        (l shr 32).toInt(),
        l.toInt()
      )
    }
  }
}