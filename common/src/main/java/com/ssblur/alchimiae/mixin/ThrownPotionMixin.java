package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.item.potions.CustomThrowAction;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
public class ThrownPotionMixin {
    @Inject(method = "onHit", at = @At("TAIL"))
    private void alchimiae$onHit(HitResult hitResult, CallbackInfo ci) {
        var self = (ThrownPotion) (Object) this;
        var item = self.getItem();
        if(item.getItem() instanceof CustomThrowAction action) {
            action.onHit(item, hitResult, self);
        }
    }
}
