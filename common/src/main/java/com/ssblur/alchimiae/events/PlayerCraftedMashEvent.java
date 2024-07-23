package com.ssblur.alchimiae.events;

import com.ssblur.alchimiae.data.IngredientEffectsSavedData;
import com.ssblur.alchimiae.data.IngredientMemorySavedData;
import com.ssblur.alchimiae.effect.AlchimiaeEffects;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerCraftedMashEvent implements PlayerEvent.CraftItem {
  @Override
  public void craft(Player player, ItemStack constructed, Container inventory) {
    if(!constructed.is(AlchimiaeItems.MASH.get())) return;
    if(!(player instanceof ServerPlayer serverPlayer)) return;
    ServerLevel level = ((ServerPlayer) player).serverLevel();

    var effects = constructed.get(DataComponents.POTION_CONTENTS);
    if(effects == null) return;

    var memory = IngredientMemorySavedData.computeIfAbsent(serverPlayer);
    var data = IngredientEffectsSavedData.computeIfAbsent(level);

    for(int i = 0; i < inventory.getContainerSize(); i++) {
      var ingredient = data.getData().get(AlchimiaeItems.ITEMS.getRegistrar().getId(inventory.getItem(i).getItem()));
      if(ingredient == null) continue;
      var intersection = ingredient.effects().stream().filter(
        effect -> effects.customEffects().stream().anyMatch(instance ->
          AlchimiaeEffects.MOB_EFFECTS.getRegistrar().getId(instance.getEffect().value()).equals(effect.effect())
        )
      ).toList();
      memory.add(serverPlayer, inventory.getItem(i).getItem(), intersection);
    }
  }
}
