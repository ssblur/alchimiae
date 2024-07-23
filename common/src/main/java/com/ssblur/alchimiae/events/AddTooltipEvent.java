package com.ssblur.alchimiae.events;

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AddTooltipEvent implements ClientTooltipEvent.Item {
  @Override
  public void append(ItemStack stack, List<Component> lines, Item.TooltipContext tooltipContext, TooltipFlag flag) {
    var data = ClientAlchemyHelper.get(stack);
    if(data != null) {
      lines.add(Component.translatable("lore.alchimiae.ingredient").withStyle(ChatFormatting.AQUA));
      for(var item: data)
        lines.add(Component.literal(" - ").append(Component.translatable(item).withStyle(ChatFormatting.BLUE)));
    }
  }
}
