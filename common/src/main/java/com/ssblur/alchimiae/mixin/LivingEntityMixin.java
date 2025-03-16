package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.resource.CustomEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
