package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.screen.menu.AlchimiaeMenus
import com.ssblur.alchimiae.screen.menu.BoilerMenu
import dev.architectury.registry.menu.MenuRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.world.inventory.MenuType

@Environment(EnvType.CLIENT)
object AlchimiaeScreens {
  fun register() {
    AlchimiaeMenus.BOILER.listen { type: MenuType<BoilerMenu> ->
      MenuRegistry.registerScreenFactory(
        type
      ) { abstractContainerMenu, inventory, component ->
        BoilerScreen(abstractContainerMenu, inventory, component)
      }
    }
  }
}
