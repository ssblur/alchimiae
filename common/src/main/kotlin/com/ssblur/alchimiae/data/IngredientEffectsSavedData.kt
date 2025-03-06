package com.ssblur.alchimiae.data

import com.google.common.base.MoreObjects
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
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class IngredientEffectsSavedData : SavedData {
  var data: MutableMap<ResourceLocation, AlchemyIngredient> = HashMap()
  var groups: Map<ResourceLocation, List<ResourceLocation>> = HashMap()

  constructor(
    data: Map<ResourceLocation?, AlchemyIngredient>,
    groups: Map<ResourceLocation, List<ResourceLocation>>
  ) {
    this.data = HashMap(data)
    this.groups = HashMap(groups)
    generate()
  }

  constructor() {
    generate()
  }

  fun generate() {
    val ingredients = HashMap<ResourceLocation, Ingredients.IngredientResource>()
    val effects = HashMap<ResourceLocation, List<ResourceLocation>>()
    val groups = HashMap<ResourceLocation, List<ResourceLocation>>()
    val random = Random()

    for ((key, group) in IngredientClasses.groups.entries) {
      val list = ArrayList(
        MoreObjects.firstNonNull(
          this.groups[key], listOf()
        )
      )
      for (effect in group.guaranteedEffects) if (!list.contains(ResourceLocation.parse(effect))) list.add(
        ResourceLocation.parse(effect)
      )

      if (list.isEmpty()) {
        val valid = Effects.effects.entries.stream().filter { effect -> effect.value.rarity <= group.rarity }.toList()
        if (!valid.isEmpty()) {
          val any = valid[random.nextInt(valid.size)]
          list.add(ResourceLocation.parse(any.value.effect))
        }
      }

      groups[key] = list
    }
    this.groups = groups

    for ((_, value) in Ingredients.ingredients.entries) {
      val item = ResourceLocation.parse(value.item)
      val list = ArrayList<ResourceLocation>()
      effects[item] = list
      ingredients[item] = value
      for (group in value.ingredientClasses) list.addAll(groups.getOrDefault(ResourceLocation.parse(group), listOf()))
      for (effect in value.guaranteedEffects) list.add(ResourceLocation.parse(effect))
    }

    var looping = true
    while (looping) {
      looping = false
      for ((_, value) in Effects.effects.entries) {
        val key = ResourceLocation.parse(value.effect)
        val valid = effects.entries.stream().filter { entry ->
          ingredients[entry.key]!!.rarity >= value.rarity && !effects[entry.key]!!
            .contains(key) && entry.value.size < 4
        }.toList()

        if (valid.isNotEmpty()) {
          val any = valid[random.nextInt(valid.size)]
          looping = true
          effects[any.key] = listOf(listOf(key), any.value).flatten()
        }
      }
    }

    for (key in ingredients.keys) if (!data.containsKey(key)) data[key] =
      AlchemyIngredient(
        ingredients[key]!!.duration,
        effects[key]!!.stream()
          .map { value: ResourceLocation -> IngredientEffect(value, 1.0f + random.nextFloat() * 0.4f) }.toList()
      )

    setDirty()
  }

  override fun save(tag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
    CODEC.encodeStart(
      NbtOps.INSTANCE,
      this
    ).ifSuccess { value: Tag? ->
      tag.put(
        "alchimiae:effects",
        value
      )
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
            AlchemyIngredient.Companion.CODEC
          ).fieldOf("data").forGetter<IngredientEffectsSavedData?> { obj: IngredientEffectsSavedData? -> obj!!.data },
          ExtraCodecs.strictUnboundedMap<ResourceLocation, List<ResourceLocation>>(
            ResourceLocation.CODEC,
            ResourceLocation.CODEC.listOf()
          ).fieldOf("groups").forGetter<IngredientEffectsSavedData> { obj: IngredientEffectsSavedData -> obj.groups }
        ).apply<IngredientEffectsSavedData?>(
          instance
        ) { data, groups -> IngredientEffectsSavedData(data, groups) }
      }

    fun load(tag: CompoundTag, provider: HolderLookup.Provider?): IngredientEffectsSavedData? {
      val input = tag["alchimiae:effects"]
      if (input != null) {
        val result = CODEC.decode(NbtOps.INSTANCE, input).result()
        if (result.isPresent && result.get().first != null) return result.get().first
      }
      return null
    }

    fun computeIfAbsent(level: ServerLevel): IngredientEffectsSavedData {
      val server = level.server.getLevel(Level.OVERWORLD)
      Objects.requireNonNull(server)
      return server!!.dataStorage.computeIfAbsent(
        Factory(
          { IngredientEffectsSavedData() },
          { tag: CompoundTag, provider: HolderLookup.Provider? -> load(tag, provider) },
          DataFixTypes.SAVED_DATA_MAP_DATA
        ),
        "alchimiae_ingredients"
      )
    }
  }
}
