package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.alchemy.ClientAlchemyHelper;
import com.ssblur.alchimiae.tooltip.IngredientTooltipComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltipImage", at = @At("RETURN"), cancellable = true)
    private void alchimiae$getTooltipImage(CallbackInfoReturnable<Optional<TooltipComponent>> info) {
        if(info.getReturnValue().isEmpty()) {
            var item = ((ItemStack) (Object) this).getItem();
            if(item == null || item == Items.AIR) return;
            var components = ClientAlchemyHelper.INSTANCE.get(item);
            if(components != null)
                info.setReturnValue(Optional.of(new IngredientTooltipComponent(components, Screen.hasShiftDown())));
        }
    }
}
