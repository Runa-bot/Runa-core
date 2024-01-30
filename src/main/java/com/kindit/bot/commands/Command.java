package com.kindit.bot.commands;

import com.kindit.bot.data.JsonConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

    public static MessageEmbed replyEmbed(String title, Color color) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setColor(color);
        return eb.build();
    }

    protected static MessageEmbed replyEmbed(String title, String description, Color color) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setColor(color);
        eb.setDescription(description);
        return eb.build();
    }

    public static MessageEmbed successfullyReplyEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully");
        eb.setColor(GOOD_COLOR);
        return eb.build();
    }

    public static MessageEmbed notSuccessfullyReplyEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Not successfully :/");
        eb.setColor(BAD_COLOR);
        return eb.build();
    }
}
