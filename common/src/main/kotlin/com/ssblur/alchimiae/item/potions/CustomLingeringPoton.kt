package com.ssblur.alchimiae.item.potions

import com.ssblur.alchimiae.data.AlchimiaeDataComponents.CUSTOM_POTION
import com.ssblur.alchimiae.entity.AlchimiaeEntities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.projectile.ThrownPotion
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.LingeringPotionItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.phys.HitResult

class CustomLingeringPoton(properties: Properties) : LingeringPotionItem(properties), CustomThrowAction {
  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
    list.removeLast()
    itemStack[CUSTOM_POTION]?.decorate(list)
  }

  override fun onHit(itemStack: ItemStack, hitResult: HitResult, potion: ThrownPotion) {
    val level = potion.level()
    val entity = AlchimiaeEntities.CUSTOM_EFFECT_CLOUD.value?.create(level) ?: return
    entity.setPos(hitResult.location)
    entity.effects = itemStack[CUSTOM_POTION]
    potion.level().addFreshEntity(entity)
  }
}