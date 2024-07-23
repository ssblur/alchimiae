package com.ssblur.alchimiae.mixin;

import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(DimensionDataStorage.class)
public interface DimensionDataStorageAccessor {
  @Accessor
  File getDataFolder();
}
