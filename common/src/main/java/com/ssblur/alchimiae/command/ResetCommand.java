package com.ssblur.alchimiae.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ssblur.alchimiae.data.IngredientEffectsSavedData;
import com.ssblur.alchimiae.data.IngredientMemorySavedData;
import com.ssblur.alchimiae.mixin.DimensionDataStorageAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ResetCommand {
  public static void register(LiteralArgumentBuilder<CommandSourceStack> command) {
    command.then(
      Commands.literal("reset")
        .requires(s -> s.hasPermission(4))
        .executes(ResetCommand::reset)
    );
  }

  private static int reset(CommandContext<CommandSourceStack> command){
    if (command.getSource().getEntity() instanceof Player player) {
      var level = command.getSource().getLevel();
      var data = IngredientEffectsSavedData.computeIfAbsent(level);
      data.getData().clear();
      data.generate();
      data.setDirty();

      var storage = ((DimensionDataStorageAccessor) level.getDataStorage()).getDataFolder();
      var target = storage.toPath().resolve("alchimiae_players").toFile();
      var files = target.listFiles();
      List<ServerPlayer> players = new ArrayList<>();
      if (files != null)
        Arrays.stream(files).sequential().forEach(file -> {
          var uuid = file.getName().substring(7, file.getName().length() - 4);
          var memory = IngredientMemorySavedData.computeIfAbsent(level, uuid);
          memory.reset(level);

          var memoryPlayer = (ServerPlayer) level.getPlayerByUUID(UUID.fromString(uuid));
          if (memoryPlayer != null) {
            memoryPlayer.sendSystemMessage(Component.translatable("command.alchimiae.reset_player"));
            memory.sync(memoryPlayer);
            players.add(memoryPlayer);
          }
        });
      for(var memoryPlayer: level.players()) {
        if(!players.contains(memoryPlayer)) {
          var memory = IngredientMemorySavedData.computeIfAbsent(memoryPlayer);
          memoryPlayer.sendSystemMessage(Component.translatable("command.alchimiae.reset_player"));
          memory.reset(level);
          memory.sync(memoryPlayer);
        }
      }

      player.sendSystemMessage(Component.translatable("command.alchimiae.reset"));
    }
    return Command.SINGLE_SUCCESS;
  }
}
