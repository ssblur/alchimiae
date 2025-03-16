package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.data.AlchimiaeDataComponents;
import com.ssblur.alchimiae.item.potions.CustomThrowAction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

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

    @Redirect(
        method = "onHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    private Object alchimiae$onHit$getOrDefault(ItemStack instance, DataComponentType dataComponentType, Object o) {
        var customEffects = instance.get(AlchimiaeDataComponents.INSTANCE.getCUSTOM_POTION());
        if(customEffects != null)
            return new PotionContents(Optional.empty(), Optional.of(customEffects.getColor()), List.of());
        return instance.getOrDefault(dataComponentType, o);
    }
}
