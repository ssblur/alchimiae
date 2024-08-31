package com.ssblur.alchimiae.screen.menu;

import com.ssblur.alchimiae.AlchimiaeMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

public class AlchimiaeMenus {
  public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.MENU);

  public static final RegistrySupplier<MenuType<BoilerMenu>> BOILER = MENUS.register("boiler",
    () -> new MenuType<>(BoilerMenu::new, FeatureFlagSet.of()));

  public static void register() {
    MENUS.register();
  }
}
