package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.effect.ArachnidMobEffect;
import com.ssblur.alchimiae.effect.ChrysopicMobEffect;
import com.ssblur.alchimiae.resource.CustomEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void alchimiae$addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        CustomEffects.INSTANCE.writeToCompoundTag((LivingEntity) (Object) this, compoundTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void alchimiae$readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        CustomEffects.INSTANCE.readFromCompoundTag((LivingEntity) (Object) this, compoundTag);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void alchimiae$onRemoved(Entity.RemovalReason removalReason, CallbackInfo ci) {
        CustomEffects.INSTANCE.remove((LivingEntity) (Object) this);
    }

    @Inject(method = "onClimbable", at = @At("RETURN"), cancellable = true)
    private void alchimiae$onClimbable(CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity) (Object) this;
        if(cir.getReturnValue() == false && ArachnidMobEffect.Companion.isAffected(self)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void alchimiae$updateFallFlying(CallbackInfo ci) {
        var self = (LivingEntity) (Object) this;
        if(self instanceof Player player)
            if (!self.onGround() && !self.isInWater() && !self.hasEffect(MobEffects.LEVITATION)) {
                if(ChrysopicMobEffect.Companion.isAffected(player)) ci.cancel();
                self.gameEvent(GameEvent.ELYTRA_GLIDE);
            }
    }
}
