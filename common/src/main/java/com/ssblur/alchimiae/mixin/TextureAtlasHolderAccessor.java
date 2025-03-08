package com.ssblur.alchimiae.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(TextureAtlasHolder.class)
public interface TextureAtlasHolderAccessor {
    @Invoker("getSprite")
    TextureAtlasSprite invokeGetSprite(ResourceLocation resourceLocation);
}
