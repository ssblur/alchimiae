package com.ssblur.alchimiae.data;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.alchimiae.events.network.client.ReceiveIngredientsNetwork;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import com.ssblur.alchimiae.mixin.DimensionDataStorageAccessor;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

public class IngredientMemorySavedData extends SavedData {
  public static final Codec<IngredientMemorySavedData> CODEC = RecordCodecBuilder.create(instance ->
    instance.group(
      ExtraCodecs.strictUnboundedMap(
        ResourceLocation.CODEC,
        Codec.STRING.listOf()
      ).fieldOf("data").forGetter(IngredientMemorySavedData::getData)
    ).apply(instance, IngredientMemorySavedData::new)
  );

  Map<ResourceLocation, List<String>> data;

  public IngredientMemorySavedData(Map<ResourceLocation, List<String>> data) {
    this.data = new HashMap<>(data);
  }

  public IngredientMemorySavedData() {
    this.data = new HashMap<>();
  }

  public void fill(ServerLevel level) {
    var effects = IngredientEffectsSavedData.computeIfAbsent(level);
    for(var key: effects.getData().keySet())
      if(!this.data.containsKey(key))
        this.data.put(key, new ArrayList<>());
    setDirty();
  }

  public void learnAll(ServerLevel level) {
    var effects = IngredientEffectsSavedData.computeIfAbsent(level);
    for(var key: effects.getData().keySet())
      this.data.put(key, effects.getData().get(key).effectsLanguageKeys());
    setDirty();
  }

  public void reset(ServerLevel level) {
    this.data.clear();
    fill(level);
  }

  public Map<ResourceLocation, List<String>> getData() {
    return this.data;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
    CODEC.encodeStart(NbtOps.INSTANCE, this).ifSuccess(value -> tag.put("alchimiae:memory", value));
    return tag;
  }

  public void add(ServerPlayer player, Item item, List<MobEffectInstance> effects) {
    var key = Objects.requireNonNull(AlchimiaeItems.ITEMS.getRegistrar().getId(item));
    var ingredientEffectsData = IngredientEffectsSavedData.computeIfAbsent(player.serverLevel());

    int checksumA = 0;
    var data = new ArrayList<>(this.data.getOrDefault(key, List.of()));
    for(var effect: data) checksumA += effect.hashCode();

    int checksumB = 0;
    var updatedData = new ArrayList<String>();
    for(var languageKey: Stream.concat(
      effects.stream().map(MobEffectInstance::getDescriptionId),
      data.stream()
    ).filter(
      effect -> ingredientEffectsData.getData().get(key).effectsLanguageKeys().contains(effect)
    ).distinct().toList()) {
      checksumB += languageKey.hashCode();
      updatedData.add(languageKey);
    }

    if(checksumA != checksumB) {
      this.data.put(key, updatedData);
      NetworkManager.sendToPlayer(player, new ReceiveIngredientsNetwork.Payload(key.toString(), updatedData));
      setDirty();
    }
  }

  public void sync(ServerPlayer player) {
    fill(player.serverLevel());
    for(var entry: data.entrySet()) {
      NetworkManager.sendToPlayer(player, new ReceiveIngredientsNetwork.Payload(entry.getKey().toString(), entry.getValue()));
    }
  }

  @Nullable
  public static IngredientMemorySavedData load(CompoundTag tag, HolderLookup.Provider provider) {
    var input = tag.get("alchimiae:memory");
    if(input != null) {
      var result = CODEC.decode(NbtOps.INSTANCE, input).result();
      if(result.isPresent() && result.get().getFirst() != null)
        return result.get().getFirst();
    }
    return null;
  }

  public static IngredientMemorySavedData computeIfAbsent(ServerPlayer player) {
    var level = player.serverLevel();
    var server = Objects.requireNonNull(level.getServer().getLevel(Level.OVERWORLD));
    var id = player.getStringUUID();
    if(id.isEmpty()) id = MoreObjects.firstNonNull(player.getDisplayName(), Component.literal("player")).getString();
    return computeIfAbsent(server, id);
  }

  public static IngredientMemorySavedData computeIfAbsent(ServerLevel level, String uuid) {
    try {
      var storage = (DimensionDataStorageAccessor) level.getDataStorage();

      if(!Files.exists(storage.getDataFolder().toPath().resolve("alchimiae_players")))
        Files.createDirectory(storage.getDataFolder().toPath().resolve("alchimiae_players"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Objects.requireNonNull(level);
    var data = level.getDataStorage().computeIfAbsent(
      new Factory<>(IngredientMemorySavedData::new, IngredientMemorySavedData::load, DataFixTypes.SAVED_DATA_MAP_DATA),
      "alchimiae_players/memory_" + uuid
    );
    data.fill(level);
    return data;
  }
}
