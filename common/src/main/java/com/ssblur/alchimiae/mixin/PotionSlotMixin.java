package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.item.potions.CustomPotion;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandMenu.PotionSlot.class)
public class PotionSlotMixin {
    @Inject(method = "mayPlaceItem", at = @At("RETURN"), cancellable = true)
    private static void alchimiae$mayPlaceItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if(itemStack.getItem() instanceof CustomPotion) {
            cir.setReturnValue(true);
        }
    }
}
