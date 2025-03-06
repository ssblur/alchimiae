package com.ssblur.alchimiae.screen.menu

import com.ssblur.alchimiae.AlchimiaeMod
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType

object AlchimiaeMenus {
  val BOILER = AlchimiaeMod.registerMenu("boiler") {
    MenuType(
      { i: Int, inventory: Inventory -> BoilerMenu(i, inventory) },
      FeatureFlagSet.of()
    )
  }

  fun register() {}
}
