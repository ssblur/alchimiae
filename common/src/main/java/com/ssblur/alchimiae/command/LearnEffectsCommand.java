package com.ssblur.alchimiae.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.alchimiae.data.IngredientMemorySavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class LearnEffectsCommand {
  public static void register(LiteralArgumentBuilder<CommandSourceStack> command){
    command.then(
      Commands.literal("learn_effects")
        .requires(s -> s.hasPermission(4))
        .executes(LearnEffectsCommand::execute)
    );
  }
  private static int execute(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof ServerPlayer player) {
      var level = command.getSource().getLevel();
      var data = IngredientMemorySavedData.computeIfAbsent(player);
      data.learnAll(level);
      data.sync(player);
    }
    return Command.SINGLE_SUCCESS;
  }
}
