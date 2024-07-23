package com.ssblur.alchimiae.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.alchimiae.alchemy.AlchemyIngredient;
import com.ssblur.alchimiae.alchemy.IngredientEffect;
import com.ssblur.alchimiae.events.reloadlisteners.EffectReloadListener;
import com.ssblur.alchimiae.events.reloadlisteners.IngredientReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class IngredientEffectsSavedData extends SavedData {
  public static final Codec<IngredientEffectsSavedData> CODEC = RecordCodecBuilder.create(instance ->
    instance.group(
      ExtraCodecs.strictUnboundedMap(ResourceLocation.CODEC, AlchemyIngredient.CODEC).fieldOf("data").forGetter(IngredientEffectsSavedData::getData)
    ).apply(instance, IngredientEffectsSavedData::new)
  );
  Map<ResourceLocation, AlchemyIngredient> data = new HashMap<>();

  public IngredientEffectsSavedData(Map<ResourceLocation, AlchemyIngredient> data) {
    this.data = new HashMap<>(data);
    generate();
  }

  public IngredientEffectsSavedData() {
    generate();
  }

  public void generate() {
    HashMap<ResourceLocation, IngredientReloadListener.IngredientResource> ingredients = new HashMap<>();
    HashMap<ResourceLocation, List<ResourceLocation>> effects = new HashMap<>();

    for(var ingredient: IngredientReloadListener.INSTANCE.ingredients.entrySet()) {
      var item = ResourceLocation.parse(ingredient.getValue().item());
      effects.put(item, new ArrayList<>());
      ingredients.put(item, ingredient.getValue());
      for(var effect: ingredient.getValue().guaranteedEffects())
        ResourceLocation.parse(effect);
    }

    boolean looping = true;
    while(looping) {
      looping = false;
      for (var effect : EffectReloadListener.INSTANCE.effects.entrySet()) {
        var key = ResourceLocation.parse(effect.getValue().effect());
        var valid = effects.entrySet().stream().filter(
          entry ->
            ingredients.get(entry.getKey()).rarity() >= effect.getValue().rarity()
              && !effects.get(entry.getKey()).contains(key)
              && entry.getValue().size() < 4
        );

        var any = valid.findAny();
        if (any.isPresent()) {
          looping = true;
          any.get().getValue().add(key);
        }
      }
    }

    Random random = new Random();
    for(var key: ingredients.keySet())
      data.put(key, new AlchemyIngredient(
        ingredients.get(key).duration(),
        effects.get(key).stream().map(value -> new IngredientEffect(value, 0.5f + random.nextFloat() * 0.5f)).toList()
      ));

    setDirty();
  }

  public Map<ResourceLocation, AlchemyIngredient> getData() {
    return data;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
    CODEC.encodeStart(NbtOps.INSTANCE, this).ifSuccess(value -> tag.put("alchimiae:effects", value));
    return tag;
  }

  public static IngredientEffectsSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
    var input = tag.get("alchimiae:effects");
    if(input != null) {
      var result = CODEC.decode(NbtOps.INSTANCE, input).result();
      if(result.isPresent() && result.get().getFirst() != null)
        return result.get().getFirst();
    }
    return null;
  }

  public static IngredientEffectsSavedData computeIfAbsent(ServerLevel level) {
    ServerLevel server = level.getServer().getLevel(Level.OVERWORLD);
    Objects.requireNonNull(server);
    return server.getDataStorage().computeIfAbsent(
      new Factory<>(IngredientEffectsSavedData::new, IngredientEffectsSavedData::load, DataFixTypes.SAVED_DATA_MAP_DATA),
      "alchimiae_ingredients"
    );
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append("IngredientEffectsSavedData:");
    for(var entry: data.entrySet()) {
      builder.append("\n - ");
      builder.append(entry.getKey());
      builder.append(": ");
      builder.append(entry.getValue());
    }
    return builder.toString();
  }
}
