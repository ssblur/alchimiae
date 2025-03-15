package com.ssblur.alchimiae.blockentity

import com.ssblur.alchimiae.AlchimiaeMod
import com.ssblur.alchimiae.block.AlchimiaeBlocks
import net.minecraft.world.level.block.entity.BlockEntityType

object AlchimiaeBlockEntities {
  val BOILER = AlchimiaeMod.registerBlockEntity("boiler") {
    BlockEntityType.Builder.of(
      { blockPos, blockState ->
        BoilerBlockEntity(
          blockPos,
          blockState
        )
      }, AlchimiaeBlocks.BOILER.get()
    ).build(null)
  }

  fun register() {}
}
