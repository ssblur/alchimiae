package com.ssblur.alchimiae.blockentity;

import com.ssblur.alchimiae.events.network.client.ParticleNetwork;
import com.ssblur.alchimiae.item.AlchimiaeItems;
import com.ssblur.alchimiae.screen.menu.BoilerMenu;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoilerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
  public static final int INGREDIENT_SLOT = 0;
  public static final int FUEL_SLOT = 1;
  public static final int RESULT_SLOT = 2;
  public static final int PROCESS_TIME = 400;

  int litTime, litDuration, processTime;
  public final ContainerData dataAccess;

  NonNullList<ItemStack> inventory;
  public BoilerBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(AlchimiaeBlockEntities.BOILER.get(), blockPos, blockState);
    inventory = NonNullList.withSize(3, ItemStack.EMPTY);

    this.dataAccess = new ContainerData() {
      public int get(int i) {
        switch (i) {
          case 0 -> {
            return litTime;
          }
          case 1 -> {
            return litDuration;
          }
          case 2 -> {
            return processTime;
          }
          default -> {
            return 0;
          }
        }
      }

      public void set(int i, int j) {
        switch (i) {
          case 0 -> litTime = j;
          case 1 -> litDuration = j;
          case 2 -> processTime = j;
        }

      }

      @Override
      public int getCount() {
        return 3;
      }
    };
    }

  @Override
  public int getContainerSize() {
    return 3;
  }

  @Override
  protected Component getDefaultName() {
    return Component.translatable("menu.alchimiae.boiler");
  }

  @Override
  protected NonNullList<ItemStack> getItems() {
    return inventory;
  }

  @Override
  protected void setItems(NonNullList<ItemStack> nonNullList) {
    inventory = nonNullList;
  }

  @Override
  public boolean isEmpty() {
    return inventory.isEmpty();
  }

  @Override
  public ItemStack getItem(int i) {
    return inventory.get(i);
  }

  @Override
  public ItemStack removeItem(int i, int j) {
    setChanged();

    var stack = inventory.get(i);
    if(j >= stack.getCount()) {
      inventory.set(i, ItemStack.EMPTY);
      return stack;
    }
    stack.shrink(j);
    return stack.copyWithCount(j);
  }

  @Override
  public ItemStack removeItemNoUpdate(int i) {
    var stack = inventory.get(i);
    inventory.set(i, ItemStack.EMPTY);
    return stack;
  }

  @Override
  public void setItem(int i, ItemStack itemStack) {
    inventory.set(i, itemStack);
  }

  @Override
  public boolean stillValid(Player player) {
    return player.distanceToSqr(getBlockPos().getCenter()) < 16;
  }

  @Override
  public void clearContent() {
    inventory = NonNullList.withSize(3, ItemStack.EMPTY);
  }

  @Override
  protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
    return new BoilerMenu(i, inventory, this);
  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    switch (direction) {
      case DOWN -> {
        return new int[] {RESULT_SLOT};
      }
      case UP -> {
        return new int[] {INGREDIENT_SLOT};
      }
      default -> {
        return new int[] {FUEL_SLOT};
      }
    }
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
    return direction == null || Arrays.stream(getSlotsForFace(direction)).anyMatch(s -> s == i);
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return Arrays.stream(getSlotsForFace(direction)).anyMatch(s -> s == i);
  }

  @Override
  public void fillStackedContents(StackedContents stackedContents) {
    for (ItemStack itemStack : inventory)
      stackedContents.accountStack(itemStack);
  }

  @Override
  protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
    super.saveAdditional(compoundTag, provider);
    ContainerHelper.saveAllItems(compoundTag, inventory, provider);

    compoundTag.putInt("processTime", processTime);
    compoundTag.putInt("litTime", litTime);
    compoundTag.putInt("litDuration", litDuration);
  }

  @Override
  protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
    super.loadAdditional(compoundTag, provider);
    ContainerHelper.loadAllItems(compoundTag, inventory, provider);

    processTime = compoundTag.getInt("processTime");
    litTime = compoundTag.getInt("litTime");
    litDuration = compoundTag.getInt("litDuration");
  }

  @SuppressWarnings("unused")
  public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
    if (blockEntity instanceof BoilerBlockEntity entity)
      entity.tick();
  }

  public void tick() {
    if(level == null) return;
    if(litTime > 0) {
      if(litTime % 3 == 0) {
        double x = getBlockPos().getX() + (10d / 16d) + level.random.nextDouble() / 10d;
        double y = getBlockPos().getY() + (5d / 16d);
        double z = getBlockPos().getZ() + (5d / 16d) + level.random.nextDouble() / 10d;
        if (level instanceof ServerLevel serverLevel)
          NetworkManager.sendToPlayers(serverLevel.players(), new ParticleNetwork.Payload(new Vec3(x, y, z), ParticleNetwork.TYPE.FLAME));
      }
      litTime--;
    }

    if(inventory.get(INGREDIENT_SLOT).is(AlchimiaeItems.MASH.get())) {
      if(litTime == 0 && AbstractFurnaceBlockEntity.isFuel(inventory.get(FUEL_SLOT))) {
        litDuration = AbstractFurnaceBlockEntity.getFuel().get(inventory.get(FUEL_SLOT).getItem()) / 2;
        litTime = litDuration;
        inventory.get(FUEL_SLOT).shrink(1);
      }

      if(litTime > 0)
        processTime = Math.min(processTime + 1, PROCESS_TIME);
      else
        processTime = Math.max(processTime - 1, 0);

      if(processTime >= PROCESS_TIME) {
        var ingredient = inventory.get(INGREDIENT_SLOT);
        var data = ingredient.get(DataComponents.POTION_CONTENTS);
        if(data != null) {
          List<MobEffectInstance> effects = new ArrayList<>();
          for(var effect: data.getAllEffects()) {
            if(effect.isInfiniteDuration() || effect.getDuration() == 0) {
              effects.add(effect);
            } else {
              effects.add(new MobEffectInstance(
                effect.getEffect(),
                effect.getDuration() / 4,
                effect.getAmplifier() + 1
              ));
            }
          }
          var outputData = new PotionContents(data.potion(), data.customColor(), effects);
          var outputStack = new ItemStack(AlchimiaeItems.CONCENTRATE);
          outputStack.set(DataComponents.POTION_CONTENTS, outputData);

          if(inventory.get(RESULT_SLOT).isEmpty()) {
            inventory.set(RESULT_SLOT, outputStack);
            inventory.get(INGREDIENT_SLOT).shrink(1);
            processTime = 0;
          } else if(
            ItemStack.isSameItemSameComponents(outputStack, inventory.get(RESULT_SLOT)) &&
              inventory.get(RESULT_SLOT).getCount() < inventory.get(RESULT_SLOT).getMaxStackSize()
          ) {
            inventory.get(RESULT_SLOT).grow(1);
            inventory.get(INGREDIENT_SLOT).shrink(1);
            processTime = 0;
          }
        }
      }
    } else {
      processTime = Math.max(processTime - 3, 0);
    }
  }
}
