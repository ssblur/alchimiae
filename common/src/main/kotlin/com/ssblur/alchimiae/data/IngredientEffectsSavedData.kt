package com.ssblur.alchimiae.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ssblur.alchimiae.alchemy.AlchemyIngredient
import com.ssblur.alchimiae.alchemy.IngredientEffect
import com.ssblur.alchimiae.resource.Effects
import com.ssblur.alchimiae.resource.IngredientClasses
import com.ssblur.alchimiae.resource.Ingredients
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import kotlin.random.Random

class IngredientEffectsSavedData : SavedData {
  var data: MutableMap<ResourceLocation?, AlchemyIngredient> = HashMap()
  var groups: Map<ResourceLocation, List<ResourceLocation>> = HashMap()

  constructor(
    data: Map<ResourceLocation?, AlchemyIngredient>,
    groups: Map<ResourceLocation, List<ResourceLocation>>
  ) {
    this.data = data.toMutableMap()
    this.groups = HashMap(groups)
    generate()
  }

  constructor() {
    generate()
  }

  fun generate() {
    val ingredients = data.toMutableMap()
    val groups = groups.toMutableMap()
    val effects = Effects.effects.filter{ it.value.rarity != null }

    IngredientClasses.groups.filter{ !groups.containsKey(it.key) }.forEach{ (key, group) ->
      if(group.guaranteedEffects.isNotEmpty()) {
        groups[key] = group.guaranteedEffects
      } else {
        val valid = effects.filter { (_, effect) ->
          effect.rarity!! <= group.rarity
        }.toList()
        if(valid.isNotEmpty()) {
          groups[key] = listOf(valid[Random.nextInt(valid.size)].first)
        }
      }
    }

    val rarity = Ingredients.ingredients.mapKeys { it.value.item }.mapValues { it.value.rarity }
    Ingredients.ingredients.forEach{ (key, value) ->
      if(!ingredients.containsKey(value.item)) {
        if(value.guaranteedEffects.isNotEmpty())
          ingredients[value.item] = AlchemyIngredient(
            value.duration,
            value.guaranteedEffects.map{ IngredientEffect(it, Random.nextFloat() + 1f) }
          )
        else
          ingredients[value.item] = AlchemyIngredient(value.duration, listOf())
      }
    }
    println(ingredients)
    println(groups)

    var looping = true
    while(looping) {
      looping = false
      effects.forEach{ (effectKey, effectValue) ->
        val validIngredients = ingredients.filter{ (key, value) ->
          value.effects.size < 4
              && (rarity[key] ?: 0f) >= effectValue.rarity!!
              && value.effects.none { it.effect == effectKey }
        }
        if(validIngredients.isNotEmpty()) {
          val ingredient = validIngredients.entries.toList()[Random.nextInt(validIngredients.size)]
          ingredients[ingredient.key] = AlchemyIngredient(
            ingredient.value.duration,
            listOf(listOf(IngredientEffect(effectKey, Random.nextFloat() + 1f)), ingredient.value.effects).flatten()
          )
          looping = true
        }
      }
    }

    this.data = ingredients
    this.groups = groups

    setDirty()
  }

  override fun save(tag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
    CODEC.encodeStart(
      NbtOps.INSTANCE,
      this
    ).ifSuccess { value ->
      tag.put("alchimiae:effects", value)
    }
    return tag
  }

  override fun toString(): String {
    val builder = StringBuilder()
    builder.append("IngredientEffectsSavedData:")
    for ((key, value) in data) {
      builder.append("\n - ")
      builder.append(key)
      builder.append(": ")
      builder.append(value)
    }
    return builder.toString()
  }

  companion object {
    val CODEC: Codec<IngredientEffectsSavedData?> =
      RecordCodecBuilder.create<IngredientEffectsSavedData?> { instance: RecordCodecBuilder.Instance<IngredientEffectsSavedData?> ->
        instance.group<Map<ResourceLocation?, AlchemyIngredient>, Map<ResourceLocation, List<ResourceLocation>>>(
          ExtraCodecs.strictUnboundedMap<ResourceLocation?, AlchemyIngredient>(
            ResourceLocation.CODEC,
            AlchemyIngredient.CODEC
          ).fieldOf("data").forGetter<IngredientEffectsSavedData?> { obj: IngredientEffectsSavedData? -> obj!!.data },
          ExtraCodecs.strictUnboundedMap<ResourceLocation, List<ResourceLocation>>(
            ResourceLocation.CODEC,
            ResourceLocation.CODEC.listOf()
          ).fieldOf("groups").forGetter<IngredientEffectsSavedData> { obj: IngredientEffectsSavedData -> obj.groups }
        ).apply<IngredientEffectsSavedData?>(
          instance
        ) { data, groups -> IngredientEffectsSavedData(data, groups) }
      }

    fun load(tag: CompoundTag): IngredientEffectsSavedData? {
      val input = tag["alchimiae:effects"]
      if (input != null) {
        val result = CODEC.decode(NbtOps.INSTANCE, input).result()
        if (result.isPresent && result.get().first != null) return result.get().first
      }
      return null
    }

    fun computeIfAbsent(level: ServerLevel): IngredientEffectsSavedData {
      val server = level.server.getLevel(Level.OVERWORLD)
      return server!!.dataStorage.computeIfAbsent(
        Factory(
          { IngredientEffectsSavedData() },
          { tag: CompoundTag, _: HolderLookup.Provider? -> load(tag) },
          DataFixTypes.SAVED_DATA_MAP_DATA
        ),
        "alchimiae_ingredients"
      )
    }
  }
}
