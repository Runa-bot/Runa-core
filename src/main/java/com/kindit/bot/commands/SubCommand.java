package com.kindit.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public abstract class SubCommand {
    public final String name;
    public final String description;

    protected SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract SubcommandData getSubCommandData();

    public abstract void interaction(SlashCommandInteractionEvent var1) throws Exception;

    void interaction(SlashCommandInteractionEvent event) throws Exception;
}
