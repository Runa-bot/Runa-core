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
    public static final Color GOOD_COLOR = new Color(0, 102, 0);
    public static final Color BAD_COLOR = new Color(102, 0, 0);
    public static final Color BOT_COLOR = new Color(100, 0, 141, 128);
    public final String userName;
    public final boolean active;
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

        this.userName = JsonConfig.getInstance().getCommand(name).get("name").toString();
        this.active = Boolean.parseBoolean(JsonConfig.getInstance().getCommand(name).get("active").toString());
    }

    protected Command(String name, String description, Subcommand[] subcommands) {
        this(name, description);

        List<Subcommand> subcommandList = new ArrayList<>();
        for (Subcommand subcommand : subcommands) {
            if (!subcommand.active) continue;
            subcommandList.add(subcommand);
        }

        this.subcommands = (Subcommand[]) subcommandList.toArray();
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
        return Commands.slash(userName, description);
    }

    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getName().equals(userName)) { return; }
        for (Subcommand command : subcommands) {
            if (event.getFullCommandName().equals(userName + " " + command.userName)) {
                command.setGuild(event.getGuild());
                command.interaction(event);
            }
        }
    }

    public void buttonInteraction(ButtonInteractionEvent event) {
        for (Subcommand command : subcommands) {
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
