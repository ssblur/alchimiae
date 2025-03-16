package com.ssblur.alchimiae.recipe

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.item.potions.Mash
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions

object PotionRecipes {
  fun mix(ingredient: ItemStack, addition: ItemStack): ItemStack? {
    val potionContents = ingredient[DataComponents.POTION_CONTENTS]
    if (addition.item is Mash && potionContents != null && potionContents.`is`(Potions.WATER)) {
      val out = ItemStack(AlchimiaeItems.POTION.get())
      out[AlchimiaeDataComponents.CUSTOM_POTION] = addition[AlchimiaeDataComponents.CUSTOM_POTION]
      out[DataComponents.ITEM_NAME] = Component.translatable("item.alchimiae.potion")
      return out
    } else if (addition.`is`(Items.GUNPOWDER) && ingredient.`is`(AlchimiaeItems.POTION.get())) {
      val out: ItemStack = ingredient.transmuteCopy(AlchimiaeItems.SPLASH_POTION.get())
      out[DataComponents.ITEM_NAME] = Component.translatable("item.alchimiae.splash_potion", out[DataComponents.ITEM_NAME])
      return out
    } else if (addition.`is`(Items.DRAGON_BREATH) && ingredient.`is`(AlchimiaeItems.POTION.get())) {
      val out: ItemStack = ingredient.transmuteCopy(AlchimiaeItems.LINGERING_POTION.get())
      out[DataComponents.ITEM_NAME] = Component.translatable("item.alchimiae.lingering_potion", out[DataComponents.ITEM_NAME])
      return out
    }
    return null
  }

  fun canMix(ingredient: ItemStack, addition: ItemStack): Boolean {
    val potionContents = ingredient[DataComponents.POTION_CONTENTS]
    val customPotionContents = ingredient[AlchimiaeDataComponents.CUSTOM_POTION]
    if (addition.item is Mash && potionContents != null && potionContents.`is`(Potions.WATER)) return true
    else if (addition.`is`(Items.DRAGON_BREATH) && customPotionContents != null && ingredient.`is`(AlchimiaeItems.POTION.get())) return true
    else if (addition.`is`(Items.GUNPOWDER) && customPotionContents != null && ingredient.`is`(AlchimiaeItems.POTION.get())) return true
    return false
  }

  fun getName(potion: ItemStack): Component {
    return Component.translatable("item.alchimiae.potion")
  }
}