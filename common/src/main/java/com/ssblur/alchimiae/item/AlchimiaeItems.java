package com.ssblur.alchimiae.item;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.block.AlchimiaeBlocks;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

@SuppressWarnings({"unused", "experimental"})
public class AlchimiaeItems {
  public static final String MOD_ID = AlchimiaeMod.MOD_ID;
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
  public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);

  public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register(
    ResourceLocation.tryBuild(MOD_ID, "scriptor_tab"),
    () ->
      CreativeTabRegistry.create(
        Component.translatable("itemGroup.alchimiae.tab"),
        () ->
          new ItemStack(AlchimiaeItems.MORTAR_AND_PESTLE.get())
      )
  );

  public static final RegistrySupplier<Item> MORTAR_AND_PESTLE = ITEMS.register("stone_mortar_and_pestle", () ->
    new GrinderItem(0.3f, new Item.Properties().durability(16).arch$tab(TAB)));
  public static final RegistrySupplier<Item> IRON_MORTAR_AND_PESTLE = ITEMS.register("iron_mortar_and_pestle", () ->
    new GrinderItem(0.5f, new Item.Properties().durability(24).arch$tab(TAB)));
  public static final RegistrySupplier<Item> GOLD_MORTAR_AND_PESTLE = ITEMS.register("gold_mortar_and_pestle", () ->
    new GrinderItem(0.8f, new Item.Properties().durability(12).arch$tab(TAB)));
  public static final RegistrySupplier<Item> DIAMOND_MORTAR_AND_PESTLE = ITEMS.register("diamond_mortar_and_pestle", () ->
    new GrinderItem(0.7f, new Item.Properties().durability(32).arch$tab(TAB)));
  public static final RegistrySupplier<Item> NETHERITE_MORTAR_AND_PESTLE = ITEMS.register("netherite_mortar_and_pestle", () ->
    new GrinderItem(1.0f, new Item.Properties().durability(64).arch$tab(TAB)));

  public static final RegistrySupplier<Item> MASH = ITEMS.register("mash", () ->
    new Mash(new Item.Properties().component(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)).arch$tab(TAB)));
  public static final RegistrySupplier<Item> CONCENTRATE = ITEMS.register("concentrate", () ->
    new Mash(new Item.Properties().component(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER)).arch$tab(TAB)));

  public static final RegistrySupplier<Item> BOILER = ITEMS.register("boiler", () ->
    new BlockItem(AlchimiaeBlocks.BOILER.get(), new Item.Properties().arch$tab(TAB)));

  public static final TagKey<Item> GRINDER = TagKey.create(Registries.ITEM, AlchimiaeMod.location("grinder"));

  public static void register() {
    ITEMS.register();
    TABS.register();

    if(Platform.getEnv() == EnvType.CLIENT) {
      ColorHandlerRegistry.registerItemColors(Mash::getColor, MASH, CONCENTRATE);
    }
  }
}
