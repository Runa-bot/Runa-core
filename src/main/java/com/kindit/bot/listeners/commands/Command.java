package com.kindit.bot.listeners.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface Command {

    String getName();

    String getDescription();

    SubCommand[] getSubCommands();

    default CommandData getCommandData() {
        if (getSubCommands() != null) {
            return Commands.slash(getName(), getDescription())
                    .addSubcommands(
                            Arrays.stream(getSubCommands())
                                    .map(SubCommand::getSubCommandData)
                                    .collect(Collectors.toList())
                    );
        }
        return Commands.slash(getName(), getDescription());
    }

    default void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getName().equals(getName())) { return; }
        for (SubCommand command : getSubCommands()) {
            if (event.getFullCommandName().equals(getName() + " " + command.getName())) {
                command.interaction(event);
            }
        }
    }
}
