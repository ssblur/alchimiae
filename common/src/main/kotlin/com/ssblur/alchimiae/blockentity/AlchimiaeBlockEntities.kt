package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.block.AlchimiaeBlocks
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object AlchimiaeBlockEntities {
  val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
    DeferredRegister.create(AlchimiaeMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE)

  val BOILER: RegistrySupplier<BlockEntityType<BoilerBlockEntity>> = BLOCK_ENTITIES.register(
    "boiler"
  ) {
    BlockEntityType.Builder.of(
      { blockPos: BlockPos?, blockState: BlockState? ->
        BoilerBlockEntity(
          blockPos,
          blockState
        )
      }, AlchimiaeBlocks.BOILER.get()
    ).build(null)
  }

  fun register() {
    BLOCK_ENTITIES.register()
  }
}
