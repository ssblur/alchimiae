package com.ssblur.alchimiae.events

import com.ssblur.alchimiae.AlchimiaeMod.location
import com.ssblur.alchimiae.item.AlchimiaeItems
import com.ssblur.unfocused.event.common.LootTablePopulateEvent
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition

object AddLootEvent {
  val pools = mutableMapOf<Holder<Item>, Pair<ResourceLocation, Float>>(
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/village/village_plains_house") to 0.2f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/village/village_snowy_house") to 0.2f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/village/village_temple") to 0.2f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/spawn_bonus_chest") to 1.0f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/shipwreck_treasure") to 0.2f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/abandoned_mineshaft") to 0.2f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/ruined_portal") to 0.2f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:chests/simple_dungeon") to 0.2f),
    AlchimiaeItems.UNIDENTIFIED_RECIPE to (location("minecraft:entities/witch") to 0.5f),
  )
  fun init() {
    LootTablePopulateEvent.register { context ->
      if (context.isBuiltin)
        for ((item, entry) in pools) {
          val (pool, rarity) = entry
          if(context.id.location() == pool) {
            val builder = LootPool.lootPool()
            builder.`when`(LootItemRandomChanceCondition.randomChance(rarity))
            builder.add(LootItem.lootTableItem(item.value()))
            context.pool += builder
          }
        }
    }
  }
}