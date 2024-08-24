package com.ssblur.alchimiae;

import com.google.common.base.Suppliers;
import com.ssblur.alchimiae.command.AlchimiaeCommand;
import com.ssblur.alchimiae.effect.AlchimiaeEffects;
import com.ssblur.alchimiae.events.AlchimiaeEvents;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import com.ssblur.alchimiae.recipe.AlchimiaeRecipes;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class AlchimiaeMod {
  public static final String MOD_ID = "alchimiae";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
  public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

  public static void init() {
    AlchimiaeEvents.register();
    AlchimiaeItems.register();
    AlchimiaeRecipes.register();
    AlchimiaeEffects.register();

    CommandRegistrationEvent.EVENT.register(AlchimiaeCommand::register);
  }

  public static ResourceLocation location(String path) {
    return ResourceLocation.tryBuild(MOD_ID, path);
  }
}
