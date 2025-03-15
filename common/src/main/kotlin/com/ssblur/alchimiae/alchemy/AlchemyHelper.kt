package com.ssblur.alchimiae.alchemy

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.data.CustomPotionEffects
import com.ssblur.alchimiae.data.IngredientEffectsSavedData
import com.ssblur.alchimiae.resource.CustomEffects
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt

object AlchemyHelper {
  fun getEffects(ingredients: List<AlchemyIngredient>, efficiency: Float): AlchemyPotion {
    val potency = HashMap<ResourceLocation, Float>()
    var duration = 0
    for (ingredient in ingredients) {
      for (effect in ingredient.effects) {
        potency[effect.effect] = potency.getOrDefault(effect.effect, 0f) + effect.potency
        duration += ingredient.duration
      }
    }

    duration /= ingredients.size
    duration *= ingredients.size + 1
    duration = Math.round((duration.toFloat()) * efficiency)
    val output = HashMap<ResourceLocation, Int>()
    potency.entries.stream()
      .filter { e: Map.Entry<ResourceLocation, Float> -> e.value >= 2 }
      .forEach { e: Map.Entry<ResourceLocation, Float> ->
        output[e.key] = (floor(sqrt(e.value.toDouble())).toInt()) - 1
      }
    return AlchemyPotion(output, duration)
  }

  fun getEffects(items: List<ItemStack>, level: ServerLevel, efficiency: Float): AlchemyPotion? {
    if (items.size <= 1) return null

    val ingredients = items.stream().filter { item: ItemStack -> !item.isEmpty }
      .map<AlchemyIngredient> { ingredient: ItemStack ->
        IngredientEffectsSavedData.computeIfAbsent(level).data[BuiltInRegistries.ITEM.getKey(ingredient.item)]
      }.toList()

    if (ingredients.stream().anyMatch { it == null }) return null
    for (i in items.indices)
      for (j in items.indices)
        if (j != i && !items[i].isEmpty && !items[j].isEmpty && items[i].item === items[j].item) return null

    return getEffects(ingredients, efficiency)
  }

  fun getPotion(items: List<ItemStack>, level: ServerLevel, efficiency: Float): List<MobEffectInstance> {
    val effects = getEffects(items, level, efficiency) ?: return listOf()
    return effects.toPotion()
  }

  fun getPotionContents(items: List<ItemStack>, level: ServerLevel, efficiency: Float = 1.0f): PotionContents {
    return PotionContents(Optional.empty(), Optional.empty(), getPotion(items, level, efficiency))
  }

  fun getCustomPotionEffects(items: List<ItemStack>, level: ServerLevel, efficiency: Float = 1.0f): CustomPotionEffects {
    val effects = getEffects(items, level, efficiency) ?: return CustomPotionEffects(listOf())
    return effects.toCustomPotion()
  }

  fun applyEffects(item: ItemStack, livingEntity: LivingEntity, durationMultiplier: Float = 1.0f) {
    item[AlchimiaeDataComponents.CUSTOM_POTION]?.let {
      for((key, duration, level) in it.effects) {
        if(BuiltInRegistries.MOB_EFFECT.containsKey(key)) {
          val effect = BuiltInRegistries.MOB_EFFECT.getHolder(key).get()
          livingEntity.addEffect(MobEffectInstance(effect, (duration * durationMultiplier).toInt(), level))
        } else if(CustomEffects.customEffects.containsKey(key)) {
          CustomEffects.applyEffect(key, livingEntity, (duration * durationMultiplier).toInt())
        }
      }
    }
  }
}
