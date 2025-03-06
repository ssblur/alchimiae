package com.ssblur.alchimiae.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.ssblur.alchimiae.data.IngredientMemorySavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer

object ForgetEffectsCommand {
  fun register(command: LiteralArgumentBuilder<CommandSourceStack?>) {
    command.then(
      Commands.literal("forget")
        .executes(::execute)
    )
  }

  private fun execute(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is ServerPlayer) {
      val player = command.source.entity as ServerPlayer
      IngredientMemorySavedData.computeIfAbsent(player).reset(player.serverLevel())
      IngredientMemorySavedData.computeIfAbsent(player).sync(player)
    }
    return Command.SINGLE_SUCCESS
  }
}
