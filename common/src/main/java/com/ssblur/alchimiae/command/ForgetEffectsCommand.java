package com.ssblur.alchimiae.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.alchimiae.data.IngredientMemorySavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class ForgetEffectsCommand {
  public static void register(LiteralArgumentBuilder<CommandSourceStack> command){
    command.then(
      Commands.literal("forget")
        .executes(ForgetEffectsCommand::execute)
    );
  }

  private static int execute(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof ServerPlayer player) {
      IngredientMemorySavedData.computeIfAbsent(player).reset(player.serverLevel());
      IngredientMemorySavedData.computeIfAbsent(player).sync(player);
    }
    return Command.SINGLE_SUCCESS;
  }
}
