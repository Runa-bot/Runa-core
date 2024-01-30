package com.kindit.bot.commands;

import com.kindit.bot.data.JsonConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public abstract class Subcommand {
    public final String userName;
    public final String description;
    final String name;
    final boolean active;
    private Guild guild;

    public Guild getGuild() {
        if (guild == null) throw new NullPointerException("Guild is null");
        return guild;
    }

    public void setGuild(Guild guild) {
        if (guild == null) throw new NullPointerException("Guild can't be null");
        else this.guild = guild;
    }

    public Subcommand(String name, String description, Command parentCommand) {
        this.name = name;
        this.description = description;

        this.userName = JsonConfig.getInstance().getSubCommand(parentCommand.name, name).get("name").toString();
        this.active = Boolean.parseBoolean(JsonConfig.getInstance().getSubCommand(parentCommand.name, name).get("active").toString());
    }

    public Subcommand(String name, String description, Command parentCommand, Guild guild) {
        this(name, description, parentCommand);
        this.guild = guild;
    }

    public abstract SubcommandData getSubCommandData();

    public abstract void interaction(SlashCommandInteractionEvent event) throws Exception;

    public void buttonInteraction(ButtonInteractionEvent event) {
    }

    protected static MessageEmbed replyEmbed(String title, Color color) {
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
    protected static MessageEmbed successfullyReplyEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully");
        eb.setColor(GOOD_COLOR);
        return eb.build();
    }

    protected static MessageEmbed notSuccessfullyReplyEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Not successfully :/");
        eb.setColor(BAD_COLOR);
        return eb.build();
    }
}
