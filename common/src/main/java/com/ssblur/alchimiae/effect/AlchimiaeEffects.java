package com.ssblur.alchimiae.effect;

import com.ssblur.alchimiae.AlchimiaeMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

@SuppressWarnings("unused")
public class AlchimiaeEffects {
  public static final String MOD_ID = AlchimiaeMod.MOD_ID;
  public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);

  public static final RegistrySupplier<MobEffect> FAMINE = EFFECTS.register("famine", () -> new FoodMobEffect(false));
  public static final RegistrySupplier<MobEffect> FEAST = EFFECTS.register("feast", () -> new FoodMobEffect(true));
  public static final RegistrySupplier<MobEffect> IMMUNE = EFFECTS.register("immune", () -> new EffectModMobEffect(true));
  public static final RegistrySupplier<MobEffect> AFFLICTED = EFFECTS.register("afflicted", () -> new EffectModMobEffect(false));
  public static final RegistrySupplier<MobEffect> STRIDE = EFFECTS.register("stride", () -> new StrideMobEffect(AlchimiaeMod.location("stride")));
  public static final RegistrySupplier<MobEffect> FUSE = EFFECTS.register("fuse", FuseMobEffect::new);
  public static final RegistrySupplier<MobEffect> LILIPUTIAN = EFFECTS.register("liliputian",
    () -> new ScaleMobEffect(-0.25, AlchimiaeMod.location("liliputian"))
  );
  public static final RegistrySupplier<MobEffect> ALICIAN = EFFECTS.register("alician",
    () -> new ScaleMobEffect(1, AlchimiaeMod.location("alician"))
  );

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
