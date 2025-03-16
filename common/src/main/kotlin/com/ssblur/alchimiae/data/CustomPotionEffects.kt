package com.ssblur.alchimiae.data

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper
import com.ssblur.alchimiae.resource.CustomEffects
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component

data class CustomPotionEffects(val effects: List<CustomEffect>, var customColor: String? = null) {
  fun decorate(list: MutableList<Component>) {
    for((key, duration, strength) in effects) {
      var component = Component.translatable("effect." + key.toLanguageKey())
      component = component.append(" ")
      component = component.append(Component.translatable("enchantment.level.${strength+1}"))
      if(duration > 1) {
        val seconds = (duration / 20) % 60
        val time = "${(duration / 20) / 60}:${if (seconds < 10) "0" else ""}$seconds"
        component = component.append(" ($time)")
      }
      list.add(component.withStyle(ClientAlchemyHelper.getDispositionStyle(key)))
    }

    if(effects.isEmpty())
      list.add(Component.translatable("lore.alchimiae.no_effects").withStyle(ChatFormatting.GRAY))
  }

  @OptIn(ExperimentalStdlibApi::class)
  val color: Int
      get() {
        try {
          if (customColor != null) return (customColor!!.toUInt(16) or 4278190080u).toInt()
        } catch (_: Exception) {}
        var color = 0
        var colorCount = 0
        for((key, _, _) in effects) {
          if(BuiltInRegistries.MOB_EFFECT.containsKey(key)) {
            val effect = BuiltInRegistries.MOB_EFFECT.get(key)!!
            colorCount++
            color = (color / colorCount) * (colorCount - 1)
            color += effect.color / colorCount
          } else if(CustomEffects.customEffects.containsKey(key)) {
            val effect = CustomEffects.customEffects[key]!!
            effect.color?.let {
              val effectColor = it.toInt(16)
              colorCount++
              color = (color / colorCount) * (colorCount - 1)
              color += effectColor / colorCount
            }
          }
        }
        if(colorCount == 0) color = 0xff0000ffu.toInt()
        customColor = color.toHexString()
        return color
      }
}