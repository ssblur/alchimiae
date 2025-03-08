package com.ssblur.alchimiae.alchemy

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object ClientAlchemyHelper {
  var EFFECTS: HashMap<Item?, List<String>?> = HashMap()

  fun reset() {
    EFFECTS = HashMap()
  }

  fun update(item: Item?, effects: List<String>?) {
    EFFECTS[item] = effects
  }

  fun update(id: String, effects: List<String>) {
    update(BuiltInRegistries.ITEM.get(ResourceLocation.parse(id)), effects)
  }

  fun get(item: Item?): List<String>? {
    return EFFECTS[item]
  }

  fun get(itemStack: ItemStack): List<String>? {
    return get(itemStack.item)
  }

  fun decorate(itemStack: ItemStack, lines: MutableList<Component?>) {
    get(itemStack.item)?.let{
      for (line in it) lines.add(Component.translatable(line))
    }
  }
}
