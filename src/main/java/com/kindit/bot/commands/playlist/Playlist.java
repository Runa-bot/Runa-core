package com.kindit.bot.commands.playlist;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.commands.playlist.subcommands.*;

public class Playlist extends Command {
    private Playlist() {
        super("playlist", "Your playlist");
    }

    public static Playlist createCommand() {
        Playlist command = new Playlist();
        command.setSubcommands(new Subcommand[] {
                new AddToUserPlaylist("add", "Add to yours playlist", command),
                new DeleteFromUserPlaylist("delete", "Delete from your playlist", command),
                new EditTrackInUserPlaylist("edit", "Edit track in your playlist", command),
                new GetUserPlaylist("get", "get", command),
                new PlayUserPlaylist("play", "Add entire playlist to queue", command)
        });
        return command;
    }
}
