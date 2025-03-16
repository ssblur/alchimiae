package com.ssblur.alchimiae.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.ssblur.alchimiae.data.IngredientEffectsSavedData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

object DumpEffectsCommand {
  fun register(command: LiteralArgumentBuilder<CommandSourceStack?>) {
    command.then(
      Commands.literal("dump_effects")
        .requires { s: CommandSourceStack -> s.hasPermission(4) }
        .executes(::execute)
    )
  }

  private fun execute(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is Player) {
      val player = command.source.entity as Player
      player.sendSystemMessage(
        Component.literal(
          IngredientEffectsSavedData.Companion.computeIfAbsent(player.level() as ServerLevel).toString()
        )
      )
    }
    return Command.SINGLE_SUCCESS
  }
}
