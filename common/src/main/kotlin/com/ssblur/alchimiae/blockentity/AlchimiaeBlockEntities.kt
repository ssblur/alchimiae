package com.ssblur.alchimiae.blockentity;

import com.ssblur.alchimiae.AlchimiaeMod;
import com.ssblur.alchimiae.block.AlchimiaeBlocks;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("ConstantConditions")
public class AlchimiaeBlockEntities {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

  public static final RegistrySupplier<BlockEntityType<BoilerBlockEntity>> BOILER = BLOCK_ENTITIES.register(
    "boiler",
    () -> BlockEntityType.Builder.of(BoilerBlockEntity::new, AlchimiaeBlocks.BOILER.get()).build(null)
  );

  public static void register() {
    BLOCK_ENTITIES.register();
  }
}
