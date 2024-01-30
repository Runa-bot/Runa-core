package com.kindit.bot.commands.playlist.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.data.JsonUserPlaylistData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class DeleteFromUserPlaylistSubCommand extends SubCommand {
    public DeleteFromUserPlaylistSubCommand() {
        super("delete", "Delete from your playlist");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(userName, description)
                .addOptions(
                        new OptionData(OptionType.STRING, "name", "Track name", true)
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        event.deferReply().setEphemeral(true).queue();

        JsonUserPlaylistData playlistData = new JsonUserPlaylistData(event.getMember().getIdLong());
        String trackName = event.getOption("name").getAsString();

        playlistData.deleteUrl(trackName);

        if (playlistData.isSet()) {
            event.getHook().sendMessageEmbeds(Command.successfullyReplyEmbed()).setEphemeral(true).queue();
        }
        else {
            event.getHook().sendMessageEmbeds(Command.notSuccessfullyReplyEmbed()).setEphemeral(true).queue();
        }
    }
}
