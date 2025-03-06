package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.data.IngredientEffectsSavedData
import com.ssblur.alchimiae.data.IngredientMemorySavedData
import com.ssblur.alchimiae.item.AlchimiaeItems
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.*

class PlayerCraftedMashEvent : PlayerEvent.CraftItem {
  override fun craft(player: Player, constructed: ItemStack, inventory: Container) {
    if (!constructed.`is`(AlchimiaeItems.MASH.get())) return
    if (player !is ServerPlayer) return
    val level = player.serverLevel()

    val effects =
      constructed.get(DataComponents.POTION_CONTENTS)
        ?: return

    val memory: IngredientMemorySavedData = IngredientMemorySavedData.Companion.computeIfAbsent(player)
    val data: IngredientEffectsSavedData = IngredientEffectsSavedData.Companion.computeIfAbsent(level)

    for (i in 0..<inventory.containerSize) {
      val id = Objects.requireNonNull(AlchimiaeItems.ITEMS.registrar.getId(inventory.getItem(i).item))
      val ingredient = data.data[id] ?: continue
      memory.add(player, inventory.getItem(i).item, effects.customEffects())
    }
  }
}
