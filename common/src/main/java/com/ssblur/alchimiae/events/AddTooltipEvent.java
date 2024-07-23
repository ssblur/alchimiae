package com.ssblur.alchimiae.events;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AddTooltipEvent implements ClientTooltipEvent.Item {
  @Override
  public void append(ItemStack stack, List<Component> lines, Item.TooltipContext tooltipContext, TooltipFlag flag) {

  }
}
