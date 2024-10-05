package com.ssblur.alchimiae.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FuseMobEffect extends InstantenousMobEffect {
  public FuseMobEffect() {
    super(MobEffectCategory.HARMFUL, 0xFFFF0000);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int i) {
    var level = livingEntity.level();
    var pos = livingEntity.position();
    level.explode(null, pos.x, pos.y, pos.z, i + 2f, Level.ExplosionInteraction.NONE);
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int i, int j) {
    return i == 1;
  }
}
