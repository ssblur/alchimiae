package com.ssblur.alchimiae.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.ssblur.alchimiae.data.IngredientEffectsSavedData
import com.ssblur.alchimiae.data.IngredientMemorySavedData
import com.ssblur.alchimiae.mixin.DimensionDataStorageAccessor
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.io.File
import java.util.*

object ResetCommand {
  fun register(command: LiteralArgumentBuilder<CommandSourceStack?>) {
    command.then(
      Commands.literal("reset")
        .requires { s: CommandSourceStack -> s.hasPermission(4) }
        .executes(::reset)
    )
  }

  private fun reset(command: CommandContext<CommandSourceStack>): Int {
    if (command.source.entity is Player) {
      val level = command.source.level
      val data: IngredientEffectsSavedData = IngredientEffectsSavedData.Companion.computeIfAbsent(level)
      data.data.clear()
      data.generate()
      data.setDirty()

      val storage = (level.dataStorage as DimensionDataStorageAccessor).dataFolder
      val target = storage!!.toPath().resolve("alchimiae_players").toFile()
      val files = target.listFiles()
      val players: MutableList<ServerPlayer> = ArrayList()
      if (files != null) Arrays.stream<File>(files).sequential().forEach { file: File ->
        val uuid = file.name.substring(7, file.name.length - 4)
        val memory: IngredientMemorySavedData = IngredientMemorySavedData.Companion.computeIfAbsent(level, uuid)
        memory.reset(level)

        val memoryPlayer = level.getPlayerByUUID(UUID.fromString(uuid)) as ServerPlayer?
        if (memoryPlayer != null) {
          memoryPlayer.sendSystemMessage(Component.translatable("command.alchimiae.reset_player"))
          memory.sync(memoryPlayer)
          players.add(memoryPlayer)
        }
      }
      for (memoryPlayer in level.players()) {
        if (!players.contains(memoryPlayer)) {
          val memory: IngredientMemorySavedData = IngredientMemorySavedData.Companion.computeIfAbsent(memoryPlayer)
          memoryPlayer.sendSystemMessage(Component.translatable("command.alchimiae.reset_player"))
          memory.reset(level)
          memory.sync(memoryPlayer)
        }
      }

      val player = command.source.entity as Player
      player.sendSystemMessage(Component.translatable("command.alchimiae.reset"))
    }
    return Command.SINGLE_SUCCESS
  }
}
