package com.ssblur.alchimiae.item.potions

import com.ssblur.alchimiae.alchemy.AlchemyHelper
import com.ssblur.alchimiae.data.AlchimiaeDataComponents.CUSTOM_POTION
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrownPotion
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SplashPotionItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import kotlin.math.sqrt

class CustomSplashPoton(properties: Properties) : SplashPotionItem(properties), CustomThrowAction {
  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
    itemStack[CUSTOM_POTION]?.decorate(list)
  }

  override fun onHit(itemStack: ItemStack, hitResult: HitResult, potion: ThrownPotion) {
    var target: Entity? = null
    if(hitResult.type == HitResult.Type.ENTITY)
      target = (hitResult as EntityHitResult).entity


    val aabb: AABB = potion.boundingBox.inflate(4.0, 2.0, 4.0)
    val list: List<LivingEntity> = potion.level().getEntitiesOfClass(LivingEntity::class.java, aabb)
    list.filter { it.isAffectedByPotions }.forEach{ livingEntity ->
      val mult = if(livingEntity == target) 1.0f else 1.0f - sqrt(potion.distanceToSqr(livingEntity)).toFloat() / 4.0f
      AlchemyHelper.applyEffects(itemStack, livingEntity, mult)
    }
  }
}