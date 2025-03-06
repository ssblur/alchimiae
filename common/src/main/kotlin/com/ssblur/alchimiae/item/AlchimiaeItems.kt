package com.ssblur.alchimiae.item

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.block.AlchimiaeBlocks
import dev.architectury.platform.Platform
import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.client.rendering.ColorHandlerRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.fabricmc.api.EnvType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions

@Suppress("unused")
object AlchimiaeItems {
  const val MOD_ID: String = AlchimiaeMod.MOD_ID
  val ITEMS: DeferredRegister<Item?> = DeferredRegister.create(
    MOD_ID, Registries.ITEM
  )
  val TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(
    MOD_ID, Registries.CREATIVE_MODE_TAB
  )

  val TAB: RegistrySupplier<CreativeModeTab> = TABS.register(
    ResourceLocation.tryBuild(MOD_ID, "alchimiae_tab")
  ) {
    CreativeTabRegistry.create(
      Component.translatable("itemGroup.alchimiae.tab")
    ) { ItemStack(MORTAR_AND_PESTLE.get()) }
  }

  val MORTAR_AND_PESTLE: RegistrySupplier<Item> = ITEMS.register(
    "stone_mortar_and_pestle"
  ) { GrinderItem(0.3f, Item.Properties().durability(16).`arch$tab`(TAB)) }
  val IRON_MORTAR_AND_PESTLE: RegistrySupplier<Item> = ITEMS.register(
    "iron_mortar_and_pestle"
  ) { GrinderItem(0.5f, Item.Properties().durability(24).`arch$tab`(TAB)) }
  val GOLD_MORTAR_AND_PESTLE: RegistrySupplier<Item> = ITEMS.register(
    "gold_mortar_and_pestle"
  ) { GrinderItem(0.8f, Item.Properties().durability(12).`arch$tab`(TAB)) }
  val DIAMOND_MORTAR_AND_PESTLE: RegistrySupplier<Item> = ITEMS.register(
    "diamond_mortar_and_pestle"
  ) { GrinderItem(0.7f, Item.Properties().durability(32).`arch$tab`(TAB)) }
  val NETHERITE_MORTAR_AND_PESTLE: RegistrySupplier<Item> = ITEMS.register(
    "netherite_mortar_and_pestle"
  ) { GrinderItem(1.0f, Item.Properties().durability(64).`arch$tab`(TAB)) }

  val MASH: RegistrySupplier<Item?> = ITEMS.register(
    "mash"
  ) {
    Mash(
      Item.Properties().component(
        DataComponents.POTION_CONTENTS,
        PotionContents(Potions.WATER)
      ).`arch$tab`(TAB)
    )
  }
  val CONCENTRATE: RegistrySupplier<Item> = ITEMS.register(
    "concentrate"
  ) {
    Mash(
      Item.Properties().component(
        DataComponents.POTION_CONTENTS,
        PotionContents(Potions.WATER)
      ).`arch$tab`(TAB)
    )
  }

  val BOILER: RegistrySupplier<Item?> = ITEMS.register(
    "boiler"
  ) {
    BlockItem(
      AlchimiaeBlocks.BOILER.get(),
      Item.Properties().`arch$tab`(TAB)
    )
  }

  val GRINDER: TagKey<Item> = TagKey.create(Registries.ITEM, AlchimiaeMod.location("grinder"))

  fun register() {
    ITEMS.register()
    TABS.register()

    if (Platform.getEnv() == EnvType.CLIENT) {
      ColorHandlerRegistry.registerItemColors({ item: ItemStack, layer: Int ->
        Mash.Companion.getColor(
          item,
          layer
        )
      }, MASH, CONCENTRATE)
    }
  }
}
