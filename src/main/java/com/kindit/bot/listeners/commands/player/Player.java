package com.kindit.bot.listeners.commands.player;

import com.kindit.bot.listeners.commands.Command;
import com.kindit.bot.listeners.commands.SubCommand;
import com.kindit.bot.listeners.commands.player.subcommands.*;

public class Player implements Command {
    private final SubCommand[] subCommands = new SubCommand[] {
            new Add(),
            new Skip(),
            new Pause(),
            new Resume(),
            new Volume(),
            new Track(),
            new Loop(),
            new Shuffel(),
            new Queue()
    };

    @Override
    public String getName() {
        return "player";
    }

    @Override
    public String getDescription() {
        return "Audio player";
    }

    @Override
    public SubCommand[] getSubCommands() {
        return subCommands;
    }
}
