package com.ssblur.alchimiae.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;

public class Mash extends Item {
  public Mash(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);
    if (potionContents != null)
      potionContents.addPotionTooltip(list::add, 1.0F, tooltipContext.tickRate());
  }

  public static int getColor(ItemStack item, int layer) {
    var data = item.get(DataComponents.POTION_CONTENTS);
    if(data != null)
      return data.getColor();
    return 0xffffffff;
  }
}
