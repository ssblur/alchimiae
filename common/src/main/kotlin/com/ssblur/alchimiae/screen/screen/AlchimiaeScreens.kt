package com.ssblur.alchimiae.screen.screen

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.screen.menu.AlchimiaeMenus
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object AlchimiaeScreens {
  fun register() {
    AlchimiaeMenus.BOILER.then {
      AlchimiaeMod.registerScreen(it) { abstractContainerMenu, inventory, component ->
        BoilerScreen(abstractContainerMenu, inventory, component)
      }
    }

    AlchimiaeMenus.ALEMBIC.then {
      AlchimiaeMod.registerScreen(it) { abstractContainerMenu, inventory, component ->
        AlembicScreen(abstractContainerMenu, inventory, component)
      }
    }

    AlchimiaeMenus.FILTER.then {
      AlchimiaeMod.registerScreen(it) { abstractContainerMenu, inventory, component ->
        FilterScreen(abstractContainerMenu, inventory, component)
      }
    }

    AlchimiaeMenus.ALCHEMINDEX.then {
      AlchimiaeMod.registerScreen(it) { abstractContainerMenu, inventory, component ->
        AlchemindexScreen(abstractContainerMenu, inventory, component)
      }
    }
  }
}
