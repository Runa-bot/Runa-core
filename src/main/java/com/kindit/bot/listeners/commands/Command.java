package com.kindit.bot.listeners.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface Command {
    SubCommand[] getSubCommands();

    CommandData getCommandData();

    void interaction(SlashCommandInteractionEvent event) throws Exception;
}
