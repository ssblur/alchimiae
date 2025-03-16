package com.ssblur.alchimiae.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

object AlchimiaeCommand {
  fun register(
    dispatcher: CommandDispatcher<CommandSourceStack>,
    @Suppress("unused_parameter") registry: CommandBuildContext?,
    @Suppress("unused_parameter") selection: Commands.CommandSelection?
  ) {
    val command = Commands.literal("alchimiae")
      .requires { s: CommandSourceStack -> s.hasPermission(4) }
      .executes(::debug)

    ResetCommand.register(command)
    DumpEffectsCommand.register(command)
    LearnEffectsCommand.register(command)
    ForgetEffectsCommand.register(command)
    GiveCustomEffectCommand.register(command)

    dispatcher.register(command)
  }

  private fun debug(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is Player) {
      val player = command.source.entity as Player
      player.sendSystemMessage(Component.translatable("command.alchimiae.debug"))

      player.sendSystemMessage(Component.literal("  ").append(Component.literal("  /alchimiae reset")))
      player.sendSystemMessage(Component.literal("  ").append(Component.translatable("command.alchimiae.reset_desc")))
      player.sendSystemMessage(Component.literal("  ").append(Component.translatable("command.alchimiae.reset_desc_2")))

      player.sendSystemMessage(Component.literal("  ").append(Component.literal("  /alchimiae learn_effects")))

      player.sendSystemMessage(Component.literal("  ").append(Component.literal("  /alchimiae forget")))
    }
    return Command.SINGLE_SUCCESS
  }
}
