package com.ssblur.alchimiae.item;

import net.minecraft.world.item.Item;

public class GrinderItem extends Item {
  float efficiency;
  public GrinderItem(float efficiency, Properties properties) {
    super(properties);
    this.efficiency = efficiency;
  }

  public float getEfficiency() {
    return efficiency;
  }
}
