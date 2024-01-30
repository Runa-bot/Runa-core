package com.kindit.bot.commands.playlist;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.commands.playlist.subcommands.*;

public class Playlist extends Command {
    public Playlist() {
        super("track-list", "Your playlist", new SubCommand[] {
                new AddToUserPlaylistSubCommand(),
                new DeleteFromUserPlaylistSubCommand(),
                new EditTrackInUserPlaylistSubCommand(),
                new GetUserPlaylistSubCommand(),
                new PlayUserPlaylistSubCommand()
        });
    }
}
