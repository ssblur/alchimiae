package com.ssblur.alchimiae.screen.menu;

import com.ssblur.alchimiae.blockentity.BoilerBlockEntity;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class BoilerMenu extends AbstractContainerMenu {
  BoilerBlockEntity boilerBlockEntity;
  ContainerData data;
  public BoilerMenu(int i, Inventory inventory) {
    super(AlchimiaeMenus.BOILER.get(), i);

    var container = new SimpleContainer(3);
    this.addSlot(new Slot(container, 0, 56, 17));
    this.addSlot(new Slot(container, 1, 56, 53));
    this.addSlot(new FurnaceResultSlot(inventory.player, container, 2, 116, 35));

    for(int j = 0; j < 3; ++j) {
      for(int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
      }
    }

    for(int j = 0; j < 9; ++j) {
      this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
    }

    data = new SimpleContainerData(3);
    addDataSlots(data);
  }

  public BoilerMenu(int i, Inventory inventory, BoilerBlockEntity boilerBlockEntity) {
    super(AlchimiaeMenus.BOILER.get(), i);

    this.addSlot(new Slot(boilerBlockEntity, 0, 56, 17));
    this.addSlot(new Slot(boilerBlockEntity, 1, 56, 53));
    this.addSlot(new FurnaceResultSlot(inventory.player, boilerBlockEntity, 2, 116, 35));

    for(int j = 0; j < 3; ++j) {
      for(int k = 0; k < 9; ++k) {
        this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
      }
    }

    for(int j = 0; j < 9; ++j) {
      this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
    }

    this.boilerBlockEntity = boilerBlockEntity;
    addDataSlots(boilerBlockEntity.dataAccess);
    data = boilerBlockEntity.dataAccess;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int i) {
    var itemStack = ItemStack.EMPTY;
    var slot = this.slots.get(i);
    if(slot.hasItem()) {
      var item = slot.getItem();
      if(i > 2) { // If in the player's inventory.
        if(item.is(AlchimiaeItems.MASH.get())) {
          if(moveItemStackTo(item, 0, 1, false)) {
            return item.copy();
          } else {
            return ItemStack.EMPTY;
          }
        } else if(AbstractFurnaceBlockEntity.isFuel(item)) {
          if(moveItemStackTo(item, 1, 2, false)) {
            return item.copy();
          } else {
            return ItemStack.EMPTY;
          }
        }
      } else { // If in machine inventory
        if(moveItemStackTo(item, 3, 39, true)) {
          return item.copy();
        } else {
          return ItemStack.EMPTY;
        }
      }
    }

    return itemStack;
  }

  @Override
  public boolean stillValid(Player player) {
    if(boilerBlockEntity != null) return boilerBlockEntity.stillValid(player);
    return false;
  }

  public float getBurnProgress() {
    return Mth.clamp((float) this.data.get(2) / (float) BoilerBlockEntity.PROCESS_TIME, 0.0f, 1.0f);
  }

  public float getLitProgress() {
    return Mth.clamp((float) this.data.get(0) / (float) this.data.get(1), 0.0f, 1.0f);
  }

  public boolean isLit() {
    return this.data.get(0) > 0;
  }
}
