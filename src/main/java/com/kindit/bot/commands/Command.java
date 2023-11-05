package com.kindit.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Command {
    public final String name;
    public final String description;
    private SubCommand[] subCommands;

    public Command(String name, String description, SubCommand[] subCommands) {
        this.name = name;
        this.description = description;
        this.subCommands = subCommands;
    }

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CommandData getCommandData() {
        if (subCommands != null) {
            return Commands.slash(name, description)
                    .addSubcommands(
                            Arrays.stream(subCommands)
                                    .map(SubCommand::getSubCommandData)
                                    .collect(Collectors.toList())
                    );
        }
        return Commands.slash(name, description);
    }

    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getName().equals(name)) { return; }
        for (SubCommand command : subCommands) {
            if (event.getFullCommandName().equals(name + " " + command.name)) {
                command.interaction(event);
            }
        }
    }

    public void buttonInteraction(ButtonInteractionEvent event) {
        for (SubCommand command : subCommands) {
            command.buttonInteraction(event);
        }
    }
}
