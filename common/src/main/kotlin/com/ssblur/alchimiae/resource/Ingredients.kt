package com.ssblur.alchimiae.resource

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object Ingredients {
  data class IngredientResource(
    val item: ResourceLocation,
    val guaranteedEffects: List<ResourceLocation>,
    val ingredientClasses: List<ResourceLocation>,
    val rarity: Float,
    val duration: Int,
    val noEffects: Boolean
  )

  val INGREDIENTS: TagKey<Item> = TagKey.create(Registries.ITEM, AlchimiaeMod.location("ingredients"))
  val ingredients = mutableMapOf<ResourceLocation, IngredientResource>()
  init {
    val set = mutableSetOf<Holder<Item>>()
    AlchimiaeMod.registerDataLoader(
      "alchimiae/ingredients",
      IngredientResource::class,
      false
    ) { ingredient, location ->
      ingredients[location] = ingredient
      val option = BuiltInRegistries.ITEM.getHolder(ingredient.item)
      if(option.isPresent) {
        set.add(option.get())
        BuiltInRegistries.ITEM.bindTags(mapOf(Pair(INGREDIENTS, set.toList())))
      } else if(option.isEmpty)
        AlchimiaeMod.LOGGER.warn("Could not bind item ${ingredient.item} to an ingredient!")
    }
  }
}