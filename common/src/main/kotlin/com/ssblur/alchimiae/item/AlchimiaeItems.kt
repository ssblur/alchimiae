package com.ssblur.alchimiae.item

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.block.AlchimiaeBlocks
import com.ssblur.unfocused.helper.ColorHelper.registerColor
import com.ssblur.unfocused.tab.CreativeTabs.registerCreativeTab
import com.ssblur.unfocused.tab.CreativeTabs.tab
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions

@Suppress("unused")
object AlchimiaeItems {
  const val MOD_ID: String = AlchimiaeMod.MOD_ID

  val TAB = AlchimiaeMod.registerCreativeTab("alchimiae_tab", "itemGroup.alchimiae.tab") {
    MASH.get()
  }

  val MORTAR_AND_PESTLE = AlchimiaeMod.registerItem("stone_mortar_and_pestle") {
    GrinderItem(0.3f, Item.Properties().durability(16))
  }.tab(TAB)
  val IRON_MORTAR_AND_PESTLE = AlchimiaeMod.registerItem("iron_mortar_and_pestle") {
    GrinderItem(0.5f, Item.Properties().durability(24))
  }.tab(TAB)
  val GOLD_MORTAR_AND_PESTLE = AlchimiaeMod.registerItem("gold_mortar_and_pestle") {
    GrinderItem(0.8f, Item.Properties().durability(12))
  }.tab(TAB)
  val DIAMOND_MORTAR_AND_PESTLE = AlchimiaeMod.registerItem("diamond_mortar_and_pestle") {
    GrinderItem(0.7f, Item.Properties().durability(32))
  }.tab(TAB)
  val NETHERITE_MORTAR_AND_PESTLE = AlchimiaeMod.registerItem("netherite_mortar_and_pestle") {
    GrinderItem(1.0f, Item.Properties().durability(64))
  }.tab(TAB)

  val MASH = AlchimiaeMod.registerItem("mash") {
    Mash(Item.Properties().component(
      DataComponents.POTION_CONTENTS,
      PotionContents(Potions.WATER)
    ))
  }
  val CONCENTRATE = AlchimiaeMod.registerItem("concentrate") {
    Mash(Item.Properties().component(
      DataComponents.POTION_CONTENTS,
      PotionContents(Potions.WATER)
    ))
  }

  val BOILER = AlchimiaeMod.registerItem("boiler") {
    BlockItem(
      AlchimiaeBlocks.BOILER.get(),
      Item.Properties()
    )
  }.tab(TAB)

  val GRINDER: TagKey<Item> = TagKey.create(Registries.ITEM, AlchimiaeMod.location("grinder"))

  fun register() {
    try { clientInit() } catch (_: NoSuchMethodError) {}
  }

  @Environment(EnvType.CLIENT)
  fun clientInit() {
    MASH.registerColor(Mash::getColor)
  }
}
