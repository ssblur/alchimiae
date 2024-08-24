package com.ssblur.alchimiae.effect;

import com.ssblur.alchimiae.AlchimiaeMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class AlchimiaeEffects {
  public static final String MOD_ID = AlchimiaeMod.MOD_ID;
  public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);

  public static final RegistrySupplier<MobEffect> FAMINE = EFFECTS.register("famine", () -> new FoodMobEffect(false));
  public static final RegistrySupplier<MobEffect> FEAST = EFFECTS.register("feast", () -> new FoodMobEffect(true));
  public static final RegistrySupplier<MobEffect> IMMUNE = EFFECTS.register("immune", () -> new EffectModMobEffect(true));
  public static final RegistrySupplier<MobEffect> AFFLICTED = EFFECTS.register("afflicted", () -> new EffectModMobEffect(false));

  public static void register() {
    EFFECTS.register();
  }

  public static Holder<MobEffect> get(RegistrySupplier<MobEffect> effect) {
    return EFFECTS.getRegistrar().getHolder(effect.getId());
  }

  public static Holder<MobEffect> get(ResourceLocation effect) {
    return EFFECTS.getRegistrar().getHolder(effect);
  }
}
