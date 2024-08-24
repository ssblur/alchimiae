package com.ssblur.alchimiae.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectModMobEffect extends MobEffect {
  boolean removeNegative;
  public EffectModMobEffect(boolean removeNegative) {
    super(removeNegative ? MobEffectCategory.BENEFICIAL : MobEffectCategory.HARMFUL, 0xCEB300FF);

    this.removeNegative = removeNegative;
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int i) {
    livingEntity.getActiveEffects().forEach(instance -> {
      var effect = instance.getEffect().value();
      if(effect instanceof EffectModMobEffect) return;
      if(effect.getCategory() != MobEffectCategory.NEUTRAL && removeNegative != effect.isBeneficial()) {
        if(instance.getDuration() == -1) return;
        int duration = instance.getDuration() - 5 * (i + 1);
        if(duration > 0)
          livingEntity.forceAddEffect(
            new MobEffectInstance(
              instance.getEffect(),
              duration,
              instance.getAmplifier(),
              instance.isAmbient(),
              instance.isVisible(),
              instance.showIcon(),
              null
            ),
            null
          );
        else
          livingEntity.removeEffect(instance.getEffect());
      }
    });
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int i, int j) {
    return (i % 5) == 0;
  }
}
