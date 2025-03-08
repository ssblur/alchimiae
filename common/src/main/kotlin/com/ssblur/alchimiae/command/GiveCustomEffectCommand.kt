package com.ssblur.alchimiae.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.ssblur.alchimiae.resource.CustomEffects
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.world.entity.LivingEntity

object GiveCustomEffectCommand {
  fun register(command: LiteralArgumentBuilder<CommandSourceStack?>) {
    command.then(
      Commands.literal("custom_effect")
        .then(Commands.argument("effect", ResourceLocationArgument.id())
          .suggests(({ _: CommandContext<CommandSourceStack?>?, builder: SuggestionsBuilder ->
            SharedSuggestionProvider.suggest(CustomEffects.customEffects.keys.map { it.toString() }, builder)
          }))
          .then(
            Commands.argument("targets", EntityArgument.entities())
              .executes(::executeNoTicks)
              .requires { s -> s.hasPermission(4) }
              .then(
                Commands.argument("ticks", IntegerArgumentType.integer(0))
                  .requires { s -> s.hasPermission(4) }
                  .executes(::execute)
              )
          )
        )
        .requires { s -> s.hasPermission(4) }
    )
  }

  private fun executeNoTicks(command: CommandContext<CommandSourceStack>): Int {
    val entities = EntityArgument.getEntities(command, "targets")
    val effect = ResourceLocationArgument.getId(command, "effect")
    for(entity in entities) if(entity is LivingEntity) CustomEffects.applyEffect(effect, entity)
    return Command.SINGLE_SUCCESS
  }

  private fun execute(command: CommandContext<CommandSourceStack>): Int {
    val entities = EntityArgument.getEntities(command, "targets")
    val effect = ResourceLocationArgument.getId(command, "effect")
    val ticks = IntegerArgumentType.getInteger(command, "ticks")
    for(entity in entities) if(entity is LivingEntity) CustomEffects.applyEffect(effect, entity, ticks)
    return Command.SINGLE_SUCCESS
  }
}
