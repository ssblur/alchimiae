package com.ssblur.alchimiae.block

import com.mojang.serialization.MapCodec
import com.ssblur.alchimiae.blockentity.AlchemyTableBlockEntity
import com.ssblur.unfocused.extension.BlockExtension.renderType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.util.StringRepresentable
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class AlchemyTableBlock: BaseEntityBlock(Properties.ofFullCopy(Blocks.CRAFTING_TABLE).noOcclusion()) {
  init {
    try { clientInit() } catch (_: NoSuchMethodError) {}
  }

  @Environment(EnvType.CLIENT)
  fun clientInit() {
    this.renderType(RenderType.cutout())
  }

  override fun codec(): MapCodec<out BaseEntityBlock?> {
    return MapCodec.unit(this)
  }

  override fun newBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
  ): BlockEntity = AlchemyTableBlockEntity(blockPos, blockState)

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING, MORTAR)
    super.createBlockStateDefinition(builder)
  }

  override fun getRenderShape(blockState: BlockState): RenderShape {
    return RenderShape.MODEL
  }

  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ) = Shapes.box(0.0, 0.0, .00, 16.0/16.0, 8.0/16.0, 16.0/16.0)


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

  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState =
    defaultBlockState()
      .setValue(FACING, blockPlaceContext.horizontalDirection.opposite)
      .setValue(MORTAR, AlchemyMortar.NONE)

  override fun <T : BlockEntity> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ): BlockEntityTicker<T> {
    return BlockEntityTicker { tickerLevel, blockPos, state, blockEntity: T ->
      val entity = level.getBlockEntity(blockPos)
      if(entity is AlchemyTableBlockEntity) entity.tick()
    }
  }

  override fun onRemove(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    blockState2: BlockState,
    movedByPiston: Boolean,
  ) {
    Containers.dropContentsOnDestroy(blockState, blockState2, level, blockPos)
    super.onRemove(blockState, level, blockPos, blockState2, movedByPiston)
  }

  companion object {
    @Suppress("unused")
    enum class AlchemyMortar: StringRepresentable {
      NONE,
      MORTAR,
      GRINDER;

      override fun getSerializedName(): String {
        return this.name.lowercase()
      }
    }

    val MORTAR: EnumProperty<AlchemyMortar> = EnumProperty.create("mortar", AlchemyMortar::class.java)
    val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
  }
}