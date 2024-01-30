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
}
