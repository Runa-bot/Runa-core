package com.kindit.bot.listeners.commands.player;

import com.kindit.bot.listeners.commands.Command;
import com.kindit.bot.listeners.commands.SubCommand;
import com.kindit.bot.listeners.commands.player.subcommands.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public class Player implements Command {
    public final String name = "player";
    private final SubCommand[] subCommands = getSubCommands();

    @Override
    public SubCommand[] getSubCommands() {
        return new SubCommand[] {
                new Add(),
                new Skip(),
                new Pause(),
                new Resume(),
                new Volume(),
                new Track()
        };
    }

    @Override
    public CommandData getCommandData() {
        List<SubcommandData> subcommandDataList = new ArrayList<>();

        for (SubCommand command : subCommands) {
            subcommandDataList.add(command.getSubCommandData());
        }

        return Commands.slash("player", "Audio player").addSubcommands(subcommandDataList);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(name)) { return; }
        for (SubCommand command : subCommands) {
            if (event.getFullCommandName().equals(name + " " + command.getName())) { command.interaction(event); }
        }
    }
}
