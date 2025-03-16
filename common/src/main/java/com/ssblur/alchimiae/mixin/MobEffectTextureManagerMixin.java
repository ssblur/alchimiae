package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.effect.AlchimiaeEffects;
import com.ssblur.alchimiae.effect.CustomMobEffect;
import com.ssblur.alchimiae.resource.CustomEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
@Mixin(MobEffectTextureManager.class)
public class MobEffectTextureManagerMixin {
    @Unique
    ResourceLocation missingno = ResourceLocation.parse("minecraft:missingno");

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void alchimiae$get(Holder<MobEffect> holder, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        var player = Minecraft.getInstance().player;
        CustomEffects.CustomEffect effect = null;
        if(player == null) return;
        if(holder.is(AlchimiaeEffects.INSTANCE.getCUSTOM_EFFECT_BENEFICIAL().ref())) {
            effect = ((CustomMobEffect) AlchimiaeEffects.INSTANCE.getCUSTOM_EFFECT_BENEFICIAL().get()).getCurrentEffect();
        } else if(holder.is(AlchimiaeEffects.INSTANCE.getCUSTOM_EFFECT_HARMFUL().ref())) {
            effect = ((CustomMobEffect) AlchimiaeEffects.INSTANCE.getCUSTOM_EFFECT_HARMFUL().get()).getCurrentEffect();
        } else if(holder.is(AlchimiaeEffects.INSTANCE.getCUSTOM_EFFECT_NEUTRAL().ref())) {
            effect = ((CustomMobEffect) AlchimiaeEffects.INSTANCE.getCUSTOM_EFFECT_NEUTRAL().get()).getCurrentEffect();
        }
        if(effect != null && effect.getLocation() != null) {
            var accessor = (TextureAtlasHolderAccessor) this;
            var sprite = accessor.invokeGetSprite(effect.getLocation());
            if(!sprite.contents().name().equals(missingno)) cir.setReturnValue(sprite);
        }
    }
}
