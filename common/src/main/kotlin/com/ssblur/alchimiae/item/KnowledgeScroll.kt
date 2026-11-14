package com.ssblur.alchimiae.item

import com.ssblur.alchimiae.data.AlchimiaeDataComponents
import com.ssblur.alchimiae.data.IngredientMemorySavedData
import com.ssblur.alchimiae.data.PlayerData
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class KnowledgeScroll(properties: Properties) : Item(properties) {
  override fun appendHoverText(
    itemStack: ItemStack,
    tooltipContext: TooltipContext,
    list: MutableList<Component?>,
    tooltipFlag: TooltipFlag
  ) {
    itemStack[AlchimiaeDataComponents.PLAYER_DATA]?.let { (name, _) ->
      list.add(Component.translatable("info.alchimiae.knowledge_scroll", name).withStyle(ChatFormatting.GRAY))
    } ?: list.add(Component.translatable("info.alchimiae.bind_scroll").withStyle(ChatFormatting.GRAY))

    super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack?>? {
    if(level is ServerLevel) {
      player.getItemInHand(interactionHand)[AlchimiaeDataComponents.PLAYER_DATA]?.let { (name, uuid) ->
        val data = IngredientMemorySavedData.computeIfAbsent(player as ServerPlayer)
        data.learnFrom(IngredientMemorySavedData.computeIfAbsent(level, uuid).data)
        data.sync(player)

        val stack = player.getItemInHand(interactionHand)
        stack.shrink(1)
        player.sendSystemMessage(Component.translatable("extra.alchimiae.knowledge_learned", name))
        return InteractionResultHolder.success(stack)
      }

      player.getItemInHand(interactionHand)[AlchimiaeDataComponents.PLAYER_DATA] = PlayerData.ofPlayer(player)
      return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }
    return super.use(level, player, interactionHand)
  }
}