package com.ssblur.alchimiae.tooltip

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.tooltip.TooltipComponent

data class IngredientTooltipComponent(val effects: List<ResourceLocation>, val shift: Boolean): TooltipComponent