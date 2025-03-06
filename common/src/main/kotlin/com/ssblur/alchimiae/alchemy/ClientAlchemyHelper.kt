package com.ssblur.alchimiae.alchemy

import com.ssblur.alchimiae.item.AlchimiaeItems
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

  fun update(id: String?, effects: List<String>?) {
    val item = AlchimiaeItems.ITEMS.registrar[ResourceLocation.parse(id)]
    EFFECTS[item] = effects
  }

  fun get(item: Item?): List<String>? {
    return EFFECTS[item]
  }

  fun get(itemStack: ItemStack): List<String>? {
    return get(itemStack.item)
  }

  fun decorate(itemStack: ItemStack, lines: MutableList<Component?>) {
    for (line in get(itemStack.item)!!) lines.add(Component.translatable(line))
  }
}
