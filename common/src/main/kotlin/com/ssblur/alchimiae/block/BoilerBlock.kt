package com.ssblur.alchimiae.block;

import com.mojang.serialization.MapCodec;
import com.ssblur.alchimiae.blockentity.BoilerBlockEntity;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BoilerBlock extends BaseEntityBlock {
  public BoilerBlock() {
    super(Properties.of().noOcclusion());

    if(!Platform.isForgeLike() && Platform.getEnv() == EnvType.CLIENT)
      RenderTypeRegistry.register(RenderType.cutout(), this);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
    if(!level.isClientSide) {
      var provider = blockState.getMenuProvider(level, blockPos);
      if(provider != null) player.openMenu(provider);
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new BoilerBlockEntity(blockPos, blockState);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return MapCodec.unit(this);
  }

  @Override
  protected RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    return BoilerBlockEntity::tick;
  }
}
