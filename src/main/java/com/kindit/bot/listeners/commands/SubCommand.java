package com.kindit.bot.listeners.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public interface SubCommand {
    String getName();

    SubcommandData getSubCommandData();

    void interaction(SlashCommandInteractionEvent event) throws Exception;
}
