package com.ssblur.alchimiae.alchemy

import com.ssblur.alchimiae.resource.CustomEffects
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object ClientAlchemyHelper {
  var EFFECTS: HashMap<Item?, List<ResourceLocation>?> = HashMap()

  fun reset() {
    EFFECTS = HashMap()
  }

  fun update(item: Item?, effects: List<ResourceLocation>?) {
    EFFECTS[item] = effects
  }

  fun update(id: ResourceLocation, effects: List<ResourceLocation>) {
    update(BuiltInRegistries.ITEM.get(id), effects)
  }

  fun get(item: Item?): List<ResourceLocation>? {
    return EFFECTS[item]
  }

  fun get(itemStack: ItemStack): List<ResourceLocation>? {
    return get(itemStack.item)
  }

  val CACHE = mutableMapOf<ResourceLocation, ChatFormatting>()
  val UNKNOWN = ResourceLocation.parse("alchimiae:unknown")
  fun getDispositionStyle(location: ResourceLocation): ChatFormatting {
    if(CACHE.containsKey(location)) return CACHE[location]!!
    if(location == UNKNOWN) return ChatFormatting.DARK_GRAY
    if(BuiltInRegistries.MOB_EFFECT.containsKey(location)) {
      val effect = BuiltInRegistries.MOB_EFFECT.get(location)!!
      if(effect.category == MobEffectCategory.HARMFUL) {
        CACHE[location] = ChatFormatting.RED
        return ChatFormatting.RED
      }
    } else if(CustomEffects.customEffects.containsKey(location)) {
      val effect = CustomEffects.customEffects[location]!!
      if(effect.category == MobEffectCategory.HARMFUL) {
        CACHE[location] = ChatFormatting.RED
        return ChatFormatting.RED
      }
    }
    CACHE[location] = ChatFormatting.BLUE
    return ChatFormatting.BLUE
  }
}
