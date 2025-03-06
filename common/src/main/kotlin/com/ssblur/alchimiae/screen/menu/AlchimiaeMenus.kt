package com.ssblur.alchimiae.screen.menu

import com.ssblur.alchimiae.AlchimiaeMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType

object AlchimiaeMenus {
  val MENUS: DeferredRegister<MenuType<*>> = DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.MENU)

  val BOILER: RegistrySupplier<MenuType<BoilerMenu>> = MENUS.register(
    "boiler"
  ) {
    MenuType(
      { i: Int, inventory: Inventory -> BoilerMenu(i, inventory) },
      FeatureFlagSet.of()
    )
  }

  fun register() {
    MENUS.register()
  }
}
