package com.ssblur.alchimiae.data

import com.google.common.base.MoreObjects
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ssblur.alchimiae.mixin.DimensionDataStorageAccessor
import com.ssblur.alchimiae.network.client.AlchimiaeNetworkS2C
import com.ssblur.alchimiae.resource.Effects
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import java.io.IOException
import java.nio.file.Files
import java.util.stream.Stream

class IngredientMemorySavedData : SavedData {
  var data: MutableMap<ResourceLocation?, List<ResourceLocation>>

  constructor(data: Map<ResourceLocation, List<ResourceLocation>>) {
    this.data = HashMap(data)
  }

  constructor() {
    this.data = HashMap()
  }

  fun fill(level: ServerLevel) {
    val effects = IngredientEffectsSavedData.computeIfAbsent(level)
    for (key in effects.data.keys) if (!data.containsKey(key)) data[key] = arrayListOf()
    setDirty()
  }

  fun learnAll(level: ServerLevel) {
    val effects: IngredientEffectsSavedData = IngredientEffectsSavedData.computeIfAbsent(level)
    for (key in effects.data.keys) data[key] = effects.data[key]?.effectKeys()!!
    setDirty()
  }

  fun reset(level: ServerLevel) {
    data.clear()
    fill(level)
  }

  override fun save(tag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
    CODEC.encodeStart(NbtOps.INSTANCE, this).ifSuccess { value ->
      tag.put("alchimiae:memory", value)
    }
    return tag
  }

  fun add(player: ServerPlayer, item: Item, effects: List<ResourceLocation>) {
    val key = BuiltInRegistries.ITEM.getKey(item)
    val ingredientEffectsData: IngredientEffectsSavedData =
      IngredientEffectsSavedData.computeIfAbsent(player.serverLevel())

    var checksumA = 0
    val data = ArrayList(data.getOrDefault(key, listOf()))
    for (effect in data) checksumA += effect.hashCode()

    var checksumB = 0
    val updatedData = arrayListOf<ResourceLocation>()
    for (location in Stream.concat(
      effects.stream(),
      data.stream()
    ).filter { effect -> ingredientEffectsData.data[key]!!.effectKeys().contains(effect) }
      .distinct().toList()) {
      checksumB += location.hashCode()
      updatedData.add(location)
    }

    if (checksumA != checksumB) {
      this.data[key] = updatedData
      val syncData = updatedData.map {
        Effects.effects[it]?.effect ?: ResourceLocation.parse("alchimiae:null")
      }.toMutableList()
      val ingredient = IngredientEffectsSavedData.computeIfAbsent(player.serverLevel()).data[key]
      ingredient?.let {
        if(syncData.size < it.effects.size)
          for(i in 0..<(it.effects.size-updatedData.size)) {
            syncData.add(ResourceLocation.parse("alchimiae:unknown"))
          }
      }
      AlchimiaeNetworkS2C.sendIngredients(
        AlchimiaeNetworkS2C.SendIngredients(key, syncData), listOf(player)
      )
      setDirty()
    }
  }

  fun sync(player: ServerPlayer) {
    fill(player.serverLevel())
    for ((key, value) in data) {
      if(key == null) continue
      val items = value.map {
        Effects.effects[it]?.effect ?: ResourceLocation.parse("alchimiae:null.${it.toLanguageKey()}")
      }.toMutableList()
      val ingredient = IngredientEffectsSavedData.computeIfAbsent(player.serverLevel()).data[key]
      ingredient?.let {
        if(items.size < it.effects.size)
          for(i in 0..<(it.effects.size-items.size)) {
            items.add(ResourceLocation.parse("alchimiae:unknown"))
          }
      }

      AlchimiaeNetworkS2C.sendIngredients(AlchimiaeNetworkS2C.SendIngredients(key, items), listOf(player))
    }
  }

  companion object {
    val CODEC: Codec<IngredientMemorySavedData?> =
      RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<IngredientMemorySavedData?> ->
        instance.group(
          ExtraCodecs.strictUnboundedMap(
            ResourceLocation.CODEC,
            ResourceLocation.CODEC.listOf()
          ).fieldOf("data")
            .forGetter { obj: IngredientMemorySavedData? -> obj!!.data }
        ).apply(
          instance
        ) { data -> IngredientMemorySavedData(data) }
      }

    fun load(tag: CompoundTag): IngredientMemorySavedData? {
      val input = tag["alchimiae:memory"]
      if (input != null) {
        val result = CODEC.decode(NbtOps.INSTANCE, input).result()
        if (result.isPresent && result.get().first != null) return result.get().first
      }
      return null
    }

    fun computeIfAbsent(player: ServerPlayer): IngredientMemorySavedData {
      val level = player.serverLevel()
      val server = level.server.getLevel(Level.OVERWORLD)!!
      var id = player.stringUUID
      if (id.isEmpty()) id = MoreObjects.firstNonNull(player.displayName, Component.literal("player")).string
      return computeIfAbsent(server, id)
    }

    fun computeIfAbsent(level: ServerLevel, uuid: String): IngredientMemorySavedData {
      try {
        val storage = level.dataStorage as DimensionDataStorageAccessor
        if (!Files.exists(storage.dataFolder.toPath().resolve("alchimiae_players")))
          Files.createDirectory(storage.dataFolder.toPath().resolve("alchimiae_players"))
      } catch (e: IOException) {
        throw RuntimeException(e)
      }

      val data = level.dataStorage.computeIfAbsent(
        Factory(
          { IngredientMemorySavedData() },
          { tag, _ -> load(tag) },
          DataFixTypes.SAVED_DATA_MAP_DATA
        ),
        "alchimiae_players/memory_$uuid"
      )
      @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
      data!!.fill(level)
      return data
    }
  }
}
