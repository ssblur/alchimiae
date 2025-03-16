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
      }, AlchimiaeBlocks.BOILER.first.get()
    ).build(null)
  }

  val ALEMBIC = AlchimiaeMod.registerBlockEntity("alembic") {
    BlockEntityType.Builder.of(
      { blockPos, blockState ->
        AlembicBlockEntity(
          blockPos,
          blockState
        )
      }, AlchimiaeBlocks.ALEMBIC.first.get()
    ).build(null)
  }

  fun register() {}
}
