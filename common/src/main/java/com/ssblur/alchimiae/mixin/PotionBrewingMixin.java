package com.ssblur.alchimiae.mixin;

import com.ssblur.alchimiae.item.AlchimiaeItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {
  @Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
  private void mashIsIngredient(ItemStack item, CallbackInfoReturnable<Boolean> info) {
    if(item.is(AlchimiaeItems.MASH.get())) info.setReturnValue(true);
  }

  @Inject(method = "mix", at = @At("RETURN"), cancellable = true)
  private void mashMix(ItemStack ingredient, ItemStack potion, CallbackInfoReturnable<ItemStack> info) {
    var potionContents = potion.get(DataComponents.POTION_CONTENTS);
    if(ingredient.is(AlchimiaeItems.MASH.get()) && potionContents != null && potionContents.is(Potions.WATER)) {
      var out = potion.copy();
      var contents = ingredient.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
      var newContents = new PotionContents(
        Optional.empty(),
        contents.customColor(),
        contents.customEffects().stream()
          .map(effect -> new MobEffectInstance(effect.getEffect(), (int) Math.round(effect.getDuration() * 0.6666), effect.getAmplifier()))
          .toList()
      );
      out.set(DataComponents.POTION_CONTENTS, newContents);
      out.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.potion"));
      info.setReturnValue(out);
    } else if(ingredient.is(Items.GUNPOWDER) && potion.is(Items.POTION) && ItemStack.isSameItemSameComponents(info.getReturnValue(), potion)) {
      var out = potion.transmuteCopy(Items.SPLASH_POTION);
      out.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.splash_potion"));
      info.setReturnValue(out);
    } else if(ingredient.is(Items.DRAGON_BREATH) && potion.is(Items.POTION) && ItemStack.isSameItemSameComponents(info.getReturnValue(), potion)) {
      var out = potion.transmuteCopy(Items.LINGERING_POTION);
      out.set(DataComponents.ITEM_NAME, Component.translatable("item.alchimiae.lingering_potion"));
      info.setReturnValue(out);
    }
  }

  @Inject(method = "hasMix", at = @At("HEAD"), cancellable = true)
  private void mashHasMix(ItemStack potion, ItemStack ingredient, CallbackInfoReturnable<Boolean> info) {
    var potionContents = potion.get(DataComponents.POTION_CONTENTS);
    if(ingredient.is(AlchimiaeItems.MASH.get()) && potionContents != null && potionContents.is(Potions.WATER))
      info.setReturnValue(true);
    else if(ingredient.is(Items.DRAGON_BREATH) && potionContents != null && potion.is(Items.POTION))
      info.setReturnValue(true);
    else if(ingredient.is(Items.GUNPOWDER) && potionContents != null && potion.is(Items.POTION))
      info.setReturnValue(true);
  }
}
