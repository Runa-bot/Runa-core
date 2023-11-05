package com.kindit.bot.commands.player;

import com.kindit.bot.commands.player.subcommands.*;
import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.SubCommand;

public class Player extends Command {
    public Player() {
        super("p", "Audio player", new SubCommand[] {
                new Add(),
                new Skip(),
                new Pause(),
                new Resume(),
                new Volume(),
                new TrackInfo(),
                new Loop(),
                new Shuffel(),
                new Queue()
        });
    }
}
