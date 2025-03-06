package com.ssblur.alchimiae

import com.google.common.base.Suppliers
import com.ssblur.alchimiae.block.AlchimiaeBlocks
import com.ssblur.alchimiae.blockentity.AlchimiaeBlockEntities
import com.ssblur.alchimiae.command.AlchimiaeCommand
import com.ssblur.alchimiae.effect.AlchimiaeEffects
import com.ssblur.alchimiae.events.AlchimiaeEvents
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.alchimiae.recipe.AlchimiaeRecipes
import com.ssblur.alchimiae.screen.menu.AlchimiaeMenus
import com.ssblur.alchimiae.screen.screen.AlchimiaeScreens
import com.ssblur.unfocused.ModInitializer
import dev.architectury.event.events.common.CommandRegistrationEvent
import dev.architectury.registry.registries.RegistrarManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.apache.logging.log4j.LogManager
import java.util.function.Supplier

object AlchimiaeMod: ModInitializer("alchimiae") {
  const val MOD_ID = "alchimiae"
  val LOGGER = LogManager.getLogger(MOD_ID)!!
  var REGISTRIES: Supplier<RegistrarManager> = Suppliers.memoize {
    RegistrarManager.get(MOD_ID)
  }

  fun init() {
    AlchimiaeEvents.register()
    AlchimiaeBlocks.register()
    AlchimiaeItems.register()
    AlchimiaeRecipes.register()
    AlchimiaeEffects.register()
    AlchimiaeBlockEntities.register()
    AlchimiaeMenus.register()

    CommandRegistrationEvent.EVENT.register(CommandRegistrationEvent { dispatcher, registry, selection ->
      AlchimiaeCommand.register(dispatcher, registry, selection)
    })
  }

  @Environment(EnvType.CLIENT)
  fun clientInit() {
    AlchimiaeScreens.register()
  }
}