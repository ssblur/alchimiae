package com.ssblur.alchimiae.block

import com.mojang.serialization.MapCodec
import com.ssblur.alchimiae.blockentity.BoilerBlockEntity
import dev.architectury.platform.Platform
import dev.architectury.registry.client.rendering.RenderTypeRegistry
import net.fabricmc.api.EnvType
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class BoilerBlock :
  BaseEntityBlock(Properties.of().noOcclusion()) {
  init {
    if (!Platform.isForgeLike() && Platform.getEnv() == EnvType.CLIENT) RenderTypeRegistry.register(
      RenderType.cutout(),
      this
    )
  }

  override fun useWithoutItem(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    player: Player,
    blockHitResult: BlockHitResult
  ): InteractionResult {
    if (!level.isClientSide) {
      val provider = blockState.getMenuProvider(level, blockPos)
      if (provider != null) player.openMenu(provider)
    }
    return InteractionResult.SUCCESS
  }

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? {
    return BoilerBlockEntity(blockPos, blockState)
  }

  override fun codec(): MapCodec<out BaseEntityBlock?> {
    return MapCodec.unit(this)
  }

  override fun getRenderShape(blockState: BlockState): RenderShape {
    return RenderShape.MODEL
  }

  override fun <T : BlockEntity> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ): BlockEntityTicker<T>? {
    return BlockEntityTicker { level, blockPos: BlockPos?, blockState, blockEntity: T ->
      BoilerBlockEntity.Companion.tick(
        level,
        blockPos,
        blockState,
        blockEntity
      )
    }
  }
}
