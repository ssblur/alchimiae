package com.ssblur.alchimiae.block

import com.mojang.serialization.MapCodec
import com.ssblur.alchimiae.blockentity.AlembicBlockEntity
import com.ssblur.unfocused.extension.BlockExtension.renderType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class AlembicBlock :
  BaseEntityBlock(Properties.of().noOcclusion()) {
  init {
    try { clientInit() } catch (_: NoSuchMethodError) {}
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(POTION1, POTION2, FACING)
    super.createBlockStateDefinition(builder)
  }

  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState =
    defaultBlockState()
      .setValue(FACING, blockPlaceContext.horizontalDirection.opposite)
      .setValue(POTION1, false)
      .setValue(POTION2, false)

  @Environment(EnvType.CLIENT)
  fun clientInit() {
    this.renderType(RenderType.cutout())
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

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
    return AlembicBlockEntity(blockPos, blockState)
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
  ): BlockEntityTicker<T> {
    return BlockEntityTicker { tickerLevel, blockPos, state, blockEntity: T ->
      AlembicBlockEntity.tick(tickerLevel, blockPos, state, blockEntity)
    }
  }

  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ) = Shapes.box(1.0/16.0, 0.0, 1.0/16.0, 15.0/16.0, 12.0/16.0, 15.0/16.0)

  override fun playerDestroy(
    level: Level,
    player: Player,
    blockPos: BlockPos,
    blockState: BlockState,
    blockEntity: BlockEntity?,
    itemStack: ItemStack
  ) {
    if (!level.isClientSide) {
      if (blockEntity is AlembicBlockEntity) {
        for (item in blockEntity.inventory) {
          val entity = ItemEntity(
            level,
            (blockPos.x + 0.5f).toDouble(),
            (blockPos.y + 0.5f).toDouble(),
            (blockPos.z + 0.5f).toDouble(),
            item
          )
          level.addFreshEntity(entity)
        }
      }
    }
    super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack)
  }

  companion object {
    val POTION1: BooleanProperty = BooleanProperty.create("potion_1")
    val POTION2: BooleanProperty = BooleanProperty.create("potion_2")
    val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
  }
}
