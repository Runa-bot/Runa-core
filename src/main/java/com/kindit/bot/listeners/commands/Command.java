package com.kindit.bot.listeners.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public abstract class Command extends ListenerAdapter {
    private String name = "noNameCommand";
    private String description = "noDescription";

    protected String getName() {
        return name;
    }

    protected String getDescription() {
        return description;
    }

    public Command(String name, String description) {
        if (!name.equals("")) { this.name = name; }
        if (!name.equals("")) { this.description = description; }
    }

    public CommandData getCommandData() {
        return Commands.slash(name, description);
    }

    public CommandData getSubCommandsData() {
        return Commands.slash(name, description);
    }

    public void interaction(SlashCommandInteractionEvent event) {}
}
