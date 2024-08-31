package com.ssblur.alchimiae.screen.screen;

import com.ssblur.alchimiae.screen.menu.AlchimiaeMenus;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AlchimiaeScreens {
  public static void register() {
    AlchimiaeMenus.BOILER.listen(type -> MenuRegistry.registerScreenFactory(type, BoilerScreen::new));
  }
}
