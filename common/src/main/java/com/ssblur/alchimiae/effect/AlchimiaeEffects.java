package com.ssblur.alchimiae.effect;

import com.ssblur.alchimiae.AlchimiaeMod;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class AlchimiaeEffects {
  public static final String MOD_ID = AlchimiaeMod.MOD_ID;
  public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);
  public static void register() {
    MOB_EFFECTS.register();
  }
}
