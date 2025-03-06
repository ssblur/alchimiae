package com.ssblur.alchimiae.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class FoodMobEffect extends InstantenousMobEffect {
  int signum;
  public FoodMobEffect(boolean refill) {
    super(refill ? MobEffectCategory.BENEFICIAL : MobEffectCategory.HARMFUL, 10);

    this.signum = refill ? 1 : -1;
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int i) {
    if(livingEntity instanceof Player player)
      player.getFoodData().eat((i + 1) * signum * 3, 0f);
    return true;
  }

  @Override
  public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity entity2, LivingEntity livingEntity, int i, double d) {
    if (livingEntity instanceof Player player)
      player.getFoodData().eat((i + 1) * signum * 3, 0f);
  }
}
