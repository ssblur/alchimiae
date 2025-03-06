package com.ssblur.alchimiae.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class AlchimiaeCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection){
    var command = Commands.literal("alchimiae")
      .requires(s -> s.hasPermission(4))
      .executes(AlchimiaeCommand::debug);

    ResetCommand.register(command);
    DumpEffectsCommand.register(command);
    LearnEffectsCommand.register(command);
    ForgetEffectsCommand.register(command);

    dispatcher.register(command);
  }

  private static int debug(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof Player player) {
      player.sendSystemMessage(Component.translatable("command.alchimiae.debug"));

      player.sendSystemMessage(Component.literal("  ").append(Component.literal("  /alchimiae reset")));
      player.sendSystemMessage(Component.literal("  ").append(Component.translatable("command.alchimiae.reset_desc")));
      player.sendSystemMessage(Component.literal("  ").append(Component.translatable("command.alchimiae.reset_desc_2")));

      player.sendSystemMessage(Component.literal("  ").append(Component.literal("  /alchimiae learn_effects")));

      player.sendSystemMessage(Component.literal("  ").append(Component.literal("  /alchimiae forget")));
    }
    return Command.SINGLE_SUCCESS;
  }
}
