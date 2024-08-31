package com.ssblur.alchimiae;

import com.google.common.base.Suppliers;
import com.ssblur.alchimiae.block.AlchimiaeBlocks;
import com.ssblur.alchimiae.blockentity.AlchimiaeBlockEntities;
import com.ssblur.alchimiae.command.AlchimiaeCommand;
import com.ssblur.alchimiae.effect.AlchimiaeEffects;
import com.ssblur.alchimiae.events.AlchimiaeEvents;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import com.ssblur.alchimiae.recipe.AlchimiaeRecipes;
import com.ssblur.alchimiae.screen.menu.AlchimiaeMenus;
import com.ssblur.alchimiae.screen.screen.AlchimiaeScreens;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrarManager;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class AlchimiaeMod {
  public static final String MOD_ID = "alchimiae";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
  public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

  public static void init() {
    AlchimiaeEvents.register();
    AlchimiaeBlocks.register();
    AlchimiaeItems.register();
    AlchimiaeRecipes.register();
    AlchimiaeEffects.register();
    AlchimiaeBlockEntities.register();
    AlchimiaeMenus.register();

    if(Platform.getEnv() == EnvType.CLIENT) {
      AlchimiaeScreens.register();
    }

    CommandRegistrationEvent.EVENT.register(AlchimiaeCommand::register);
  }

  public static ResourceLocation location(String path) {
    return ResourceLocation.tryBuild(MOD_ID, path);
  }
}
