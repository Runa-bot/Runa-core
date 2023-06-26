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
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public SubCommand[] getSubCommands() {
        return new SubCommand[] {
                new Add(),
                new Skip(),
                new Pause(),
                new Resume(),
                new Volume(),
                new Track(),
                new Loop()
        };
    }

    @Override
    public CommandData getCommandData() {
        List<SubcommandData> subcommandDataList = new ArrayList<>();

        for (SubCommand command : getSubCommands()) {
            subcommandDataList.add(command.getSubCommandData());
        }

        return Commands.slash("player", "Audio player").addSubcommands(subcommandDataList);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getName().equals(getName())) { return; }
        for (SubCommand command : getSubCommands()) {
            if (event.getFullCommandName().equals(getName() + " " + command.getName())) { command.interaction(event); }
        }
    }
}
