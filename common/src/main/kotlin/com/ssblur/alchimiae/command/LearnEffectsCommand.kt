package com.ssblur.alchimiae.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.ssblur.alchimiae.data.IngredientEffectsSavedData
import com.ssblur.alchimiae.data.IngredientMemorySavedData
import com.ssblur.alchimiae.resource.Effects
import com.ssblur.alchimiae.resource.Ingredients
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import kotlin.jvm.optionals.getOrNull

object LearnEffectsCommand {
  fun register(command: LiteralArgumentBuilder<CommandSourceStack?>) {
    command.then(
      Commands.literal("learn")
        .requires { s: CommandSourceStack -> s.hasPermission(4) }
        .then(
          Commands.literal("all")
            .executes(::executeAll)
        )
        .then(
          Commands.literal("item")
            .then(
              Commands.argument("item", ResourceLocationArgument.id())
                .suggests(({ _: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder ->
                    SharedSuggestionProvider.suggest( Ingredients.ingredientSet.mapNotNull { it.unwrapKey().getOrNull()?.location()?.toString() }, builder)
                }))
                .executes(::executeItem)
            )
        )
        .then(
          Commands.literal("effect")
            .then(
              Commands.argument("effect", ResourceLocationArgument.id())
                .suggests(({ _: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder ->
                  SharedSuggestionProvider.suggest( Effects.effects.keys.map { it.toString() }, builder)
                }))
              .executes(::executeEffect)
            )
        )
        .then(
          Commands.literal("group")
            .then(
              Commands.argument("group", ResourceLocationArgument.id())
                .suggests(({ context: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder ->
                  val level = context?.source?.level!!
                  SharedSuggestionProvider.suggest( IngredientEffectsSavedData.computeIfAbsent(level).groups.mapNotNull { (k,v) -> if (v.isNotEmpty()) k.toString() else null }, builder)
                }))
                .executes(::executeGroup)
            )
        )
    )
  }

  private fun executeAll(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is ServerPlayer) {
      val player = command.source.entity as ServerPlayer
      val level = command.source.level
      val data: IngredientMemorySavedData = IngredientMemorySavedData.computeIfAbsent(player)
      data.learnAll(level)
      data.sync(player)
    }
    return Command.SINGLE_SUCCESS
  }

  private fun executeItem(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is ServerPlayer) {
      val player = command.source.entity as ServerPlayer
      val item = ResourceLocationArgument.getId(command, "item")
      val data = IngredientEffectsSavedData.computeIfAbsent(player.level() as ServerLevel)
      val playerData: IngredientMemorySavedData = IngredientMemorySavedData.computeIfAbsent(player)
      val ingredient = data.data[item]
      if (ingredient == null || ingredient.effects.isEmpty()) return -1
      playerData.add(player, item, ingredient.effectKeys().map { Effects.effects[it]!!.effect })
    }
    return Command.SINGLE_SUCCESS
  }

  private fun executeEffect(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is ServerPlayer) {
      val player = command.source.entity as ServerPlayer
      val effect = ResourceLocationArgument.getId(command, "effect")
      val data = IngredientEffectsSavedData.computeIfAbsent(player.level() as ServerLevel)
      val playerData: IngredientMemorySavedData = IngredientMemorySavedData.computeIfAbsent(player)
      val realEffect = Effects.effects[effect]?.effect ?: return -1
      val items = data.data.mapNotNull { (k,v)-> if (v.effectKeys().contains(effect)) k else null }
      for (item in items){
        playerData.add(player, item, listOf(realEffect))
      }
    }
    return Command.SINGLE_SUCCESS
  }

  private fun executeGroup(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is ServerPlayer) {
      val player = command.source.entity as ServerPlayer
      val group = ResourceLocationArgument.getId(command, "group")
      val data = IngredientEffectsSavedData.computeIfAbsent(player.level() as ServerLevel)
      val playerData: IngredientMemorySavedData = IngredientMemorySavedData.computeIfAbsent(player)
      val items = Ingredients.ingredients.values.mapNotNull { if (it.ingredientClasses.contains(group)) it.item else null }
      val groupEffects = data.groups[group]?.map { Effects.effects[it]!!.effect } ?: return -1
      for (item in items) {
        playerData.add(player, item, groupEffects)
      }
    }
    return Command.SINGLE_SUCCESS
  }
}
