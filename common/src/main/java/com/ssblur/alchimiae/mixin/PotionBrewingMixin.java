package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.item.potions.Mash;
import com.ssblur.alchimiae.recipe.PotionRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {
  @Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
  private void alchimiae$isIngredient(ItemStack item, CallbackInfoReturnable<Boolean> info) {
    if(item.getItem() instanceof Mash) info.setReturnValue(true);
  }

  @Inject(method = "mix", at = @At("RETURN"), cancellable = true)
  private void alchimiae$mix(ItemStack ingredient, ItemStack potion, CallbackInfoReturnable<ItemStack> info) {
    var out = PotionRecipes.INSTANCE.mix(potion, ingredient);
    if(out != null) info.setReturnValue(out);
  }

  @Inject(method = "hasMix", at = @At("HEAD"), cancellable = true)
  private void alchimiae$hasMix(ItemStack potion, ItemStack ingredient, CallbackInfoReturnable<Boolean> info) {
    if(PotionRecipes.INSTANCE.canMix(potion, ingredient)) info.setReturnValue(true);
  }
}
