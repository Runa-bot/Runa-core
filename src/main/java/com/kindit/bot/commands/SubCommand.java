package com.kindit.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public abstract class SubCommand {
    public static final Color GOOD_COLOR = new Color(0, 102, 0);
    public static final Color BAD_COLOR = new Color(102, 0, 0);
    public static final Color BOT_COLOR = new Color(100, 0, 141, 128);
    public final String name;
    public final String description;
    private Guild guild;

    public Guild getGuild() {
        if (guild == null) throw new NullPointerException("Guild is null");
        return guild;
    }

    public void setGuild(Guild guild) {
        if (guild == null) throw new NullPointerException("Guild can't be null");
        else this.guild = guild;
    }

    public SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
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
