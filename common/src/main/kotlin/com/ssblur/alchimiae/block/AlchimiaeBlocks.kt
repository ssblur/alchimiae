package com.ssblur.alchimiae.block

import com.ssblur.alchimiae.AlchimiaeMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

object AlchimiaeBlocks {
  val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.BLOCK)

  val BOILER: RegistrySupplier<Block?> = BLOCKS.register(
    "boiler"
  ) { BoilerBlock() }

  fun register() {
    BLOCKS.register()
  }
}
