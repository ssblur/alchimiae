package com.ssblur.alchimiae.item

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.data.IngredientEffectsSavedData
import com.ssblur.alchimiae.data.IngredientMemorySavedData
import com.ssblur.alchimiae.data.PotionRecipe
import com.ssblur.alchimiae.resource.Effects
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class UnidentifiedRecipe(properties: Properties) : Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component?>,
    tooltipFlag: TooltipFlag
  ) {
    list.add(Component.translatable("lore.alchimiae.identify_recipe"))
    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack?>? {
    val item = player.getItemInHand(interactionHand)
    if(level.isClientSide) return InteractionResultHolder.success(item)

    // pick a potion effect, pick 2-3 random items that possess it, produce an identified recipe with them
    val effect = Effects.effects.values.random().effect
    val count = level.random.nextInt(3) + 2
    val data = IngredientEffectsSavedData.computeIfAbsent(level as ServerLevel)
    var items = data.data.entries.filter { (k, v) ->
      effect in v.effects.map { it.effect } && BuiltInRegistries.ITEM.containsKey(k!!)
    }.shuffled()
    if(items.size > count) {
      items = items.subList(0, count)
    } else if(items.size <= 1) {
      return use(level, player, interactionHand)
    }
    val effectCount = mutableMapOf<ResourceLocation, Int>()
    items.forEach { (_, v) ->
      v.effects.forEach {
        effectCount[it.effect] = (effectCount[it.effect] ?: 0) + 1
      }
    }
    val effects = effectCount.filter { (_, v) -> v > 1 }.map { (k, _) ->
      Effects.effects[k]!!.effect
    }
    val itemLocations = items.mapNotNull { (k, _) -> k }

    val out = ItemStack(AlchimiaeItems.IDENTIFIED_RECIPE.get())
    out[AlchimiaeDataComponents.POTION_RECIPE] = PotionRecipe(effects, itemLocations)
    if (!out.isEmpty && !player.addItem(out))
      level.addFreshEntity(ItemEntity(level, player.x, player.y, player.z, out))

    if(!player.isCreative) item.shrink(1)

    player.sendSystemMessage(Component.translatable("extra.alchimiae.recipe_uncovered").withStyle(ChatFormatting.ITALIC))
    player.sendSystemMessage(Component.translatable("extra.alchimiae.recipe_learned").withStyle(ChatFormatting.ITALIC))

    // add the effects in this potion to the user's knowledge
    val memory = IngredientMemorySavedData.computeIfAbsent(player as ServerPlayer)
    for(location in itemLocations) {
      memory.add(player, location, effects) // don't need to filter effects ourselves
      // I was kind to myself and made the memory data add function do that already
    }

    return super.use(level, player, interactionHand)
  }
}