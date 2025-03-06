package com.ssblur.alchimiae.alchemy;

import com.ssblur.alchimiae.item.AlchimiaeItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ClientAlchemyHelper {
  public static HashMap<Item, List<String>> EFFECTS = new HashMap<>();

  public static void reset() {
    EFFECTS = new HashMap<>();
  }

  public static void update(Item item, List<String> effects) {
    EFFECTS.put(item, effects);
  }

  public static void update(String id, List<String> effects) {
    var item = AlchimiaeItems.ITEMS.getRegistrar().get(ResourceLocation.parse(id));
    EFFECTS.put(item, effects);
  }

  public static List<String> get(Item item) {
    return EFFECTS.get(item);
  }

  public static List<String> get(ItemStack itemStack) {
    return get(itemStack.getItem());
  }

  public static void decorate(ItemStack itemStack, List<Component> lines) {
    for(var line: get(itemStack.getItem()))
      lines.add(Component.translatable(line));
  }
}
