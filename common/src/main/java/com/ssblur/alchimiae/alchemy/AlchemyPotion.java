package com.ssblur.alchimiae.alchemy;

import com.ssblur.alchimiae.effect.AlchimiaeEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record AlchemyPotion(Map<ResourceLocation, Integer> effects, int duration) {
  public List<MobEffectInstance> toPotion() {
    List<MobEffectInstance> list = new ArrayList<>();
    for(var entry: effects.entrySet()) {
      var effect = AlchimiaeEffects.MOB_EFFECTS.getRegistrar().getHolder(entry.getKey());
      if(effect != null)
        list.add(new MobEffectInstance(
          effect,
          effect.value().isInstantenous() ? 0 : duration,
          entry.getValue()
        ));
    }
    return list;
  }
}
