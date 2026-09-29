package com.ssblur.alchimiae.data

import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import kotlin.random.Random

data class PotionRecipe(val effects: List<ResourceLocation>, val items: List<ResourceLocation>, val customName: String? = null) {
  val name: Component
    get() {
      if(customName != null) return Component.translatable(customName)
      when (effects.size) {
        1 -> {
          return Component.translatable(
            "item.alchimiae.recipe",
            Component.translatable(effects[0].toLanguageKey("effect"))
          )
        }
        2 -> {
          return Component.translatable(
            "item.alchimiae.recipe_join",
            Component.translatable(effects[0].toLanguageKey("effect")),
            Component.translatable(effects[1].toLanguageKey("effect"))
          )
        }
        else -> {
          val random = Random(effects.hashCode() + items.hashCode())
          val pick = random.nextInt(4) + 1
          return Component.translatable(
            "item.alchimiae.recipe",
            Component.translatable("item.alchimiae.recipe_too_many_$pick")
          )
        }
      }
    }

  fun decorateHoverText(
    itemStack: ItemStack,
    list: MutableList<Component?>,
  ) {
    list.add(Component.translatable("lore.alchimiae.recipe_contents"))
    for(item in items) {
      list.add(Component.translatable(
        "lore.alchimiae.recipe_contents_2",
        Component.translatable(
          if(I18n.exists(item.toLanguageKey("block"))) item.toLanguageKey("block")
          else item.toLanguageKey("item")
        )
      ))
    }
  }
}