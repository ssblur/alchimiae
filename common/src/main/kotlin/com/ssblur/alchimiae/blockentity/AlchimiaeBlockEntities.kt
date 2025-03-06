package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.block.AlchimiaeBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object AlchimiaeBlockEntities {
  val BOILER = AlchimiaeMod.registerBlockEntity("boiler") {
    BlockEntityType.Builder.of(
      { blockPos: BlockPos?, blockState: BlockState? ->
        BoilerBlockEntity(
          blockPos,
          blockState
        )
      }, AlchimiaeBlocks.BOILER.get()
    ).build(null)
  }

  fun register() {}
}
