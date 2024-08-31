package com.ssblur.alchimiae.block;

import com.ssblur.alchimiae.AlchimiaeMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class AlchimiaeBlocks {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.BLOCK);

  public static final RegistrySupplier<Block> BOILER = BLOCKS.register("boiler", BoilerBlock::new);

  public static void register() {
    BLOCKS.register();
  }
}
