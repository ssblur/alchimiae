package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.effect.ChrysopicMobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "tryToStartFallFlying", at = @At("RETURN"), cancellable = true)
    private void alchimiae$tryToStartFallFlying(CallbackInfoReturnable<Boolean> cir) {
        var self = (Player) (Object) this;
        if (!self.onGround() && !self.isFallFlying() && !self.isInWater() && !self.hasEffect(MobEffects.LEVITATION))
            if(ChrysopicMobEffect.Companion.isAffected(self)) {
                self.startFallFlying();
                cir.setReturnValue(true);
            }
    }
}
