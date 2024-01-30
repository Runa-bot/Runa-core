package com.kindit.bot.commands.player;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.commands.player.subcommands.*;

public class Player extends Command {
    private Player() {
        super("player", "Audio player");
    }

    public static Player createCommand() {
        Player command = new Player();
        command.setSubcommands(new Subcommand[] {
                new AddTrack("add", "Add track in queue", command),
                new SkipTrack("skip", "Skip track", command),
                new PauseTrack("pause", "Pauses it", command),
                new ResumeTrack("resume", "Resuming", command),
                new PlayerVolume("volume", "Sound volume", command),
                new TrackInfo("track-info", "Gets track information", command),
                new LoopTrack("loop", "loop track", command),
                new ShuffelQueue("shuffel", "Shuffles the queue", command),
                new GetQueue("queue", "Get the whole queue", command)
        });
        return command;
    }
}
