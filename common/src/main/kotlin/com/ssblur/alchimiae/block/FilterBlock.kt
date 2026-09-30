package com.ssblur.alchimiae.block

import com.mojang.serialization.MapCodec
import com.ssblur.alchimiae.blockentity.FilterBlockEntity
import com.ssblur.unfocused.extension.BlockExtension.renderType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class FilterBlock :
  BaseEntityBlock(Properties.of().noOcclusion()) {
  init {
    try {
      clientInit()
    } catch (_: NoSuchMethodError) {}
  }

  @Environment(EnvType.CLIENT)
  fun clientInit() {
    this.renderType(RenderType.cutout())
  }
  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FULLNESS, STATE)
    super.createBlockStateDefinition(builder)
  }

  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState =
    defaultBlockState()
      .setValue(FULLNESS, false)
      .setValue(STATE, State.EMPTY)

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
    return FilterBlockEntity(blockPos, blockState)
  }

  override fun codec(): MapCodec<out BaseEntityBlock?> {
    return MapCodec.unit(this)
  }

  override fun getRenderShape(blockState: BlockState): RenderShape {
    return RenderShape.MODEL
  }

  override fun playerDestroy(
    level: Level,
    player: Player,
    blockPos: BlockPos,
    blockState: BlockState,
    blockEntity: BlockEntity?,
    itemStack: ItemStack
  ) {
    if (!level.isClientSide) {
      if (blockEntity is FilterBlockEntity) {
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

  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ) = Shapes.box(1.0/16.0, 0.0, 1.0/16.0, 15.0/16.0, 16.0/16.0, 15.0/16.0)

  override fun <T : BlockEntity> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ): BlockEntityTicker<T> {
    return BlockEntityTicker { tickerLevel, blockPos, state, blockEntity: T ->
      val entity = level.getBlockEntity(blockPos)
      if(entity is FilterBlockEntity) entity.tick()
    }
  }

  enum class State: StringRepresentable {
    EMPTY,
    FILTER,
    FILTER_AND_CHARCOAL;

    override fun getSerializedName(): String {
      return this.name.lowercase()
    }
  }

  companion object {
    val STATE: EnumProperty<State> = EnumProperty.create("state", State::class.java)
    val FULLNESS: BooleanProperty = BooleanProperty.create("full")
  }
}
