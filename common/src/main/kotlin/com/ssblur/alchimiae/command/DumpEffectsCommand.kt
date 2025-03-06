package com.ssblur.alchimiae.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.alchimiae.data.IngredientEffectsSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class DumpEffectsCommand {
  public static void register(LiteralArgumentBuilder<CommandSourceStack> command){
    command.then(
      Commands.literal("dump_effects")
        .requires(s -> s.hasPermission(4))
        .executes(DumpEffectsCommand::execute)
    );
  }
  private static int execute(CommandContext<CommandSourceStack> command){
    if(command.getSource().getEntity() instanceof Player player) {
        player.sendSystemMessage(Component.literal(IngredientEffectsSavedData.computeIfAbsent((ServerLevel) player.level()).toString()));
    }
    return Command.SINGLE_SUCCESS;
  }
}
