package com.ssblur.alchimiae.item.potions

import net.minecraft.world.entity.projectile.ThrownPotion
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.HitResult

interface CustomThrowAction {
  fun onHit(itemStack: ItemStack, hitResult: HitResult, potion: ThrownPotion)
}