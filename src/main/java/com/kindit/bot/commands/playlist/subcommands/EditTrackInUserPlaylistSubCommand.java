package com.kindit.bot.commands.playlist.subcommands;

import com.kindit.bot.commands.SubCommand;
import com.kindit.bot.data.JsonUserPlaylistData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class EditTrackInUserPlaylistSubCommand extends SubCommand {
    public EditTrackInUserPlaylistSubCommand() {
        super("edit", "Edit track in your playlist");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, description)
                .addOptions(
                        new OptionData(OptionType.STRING, "name", "Your name for url", true),
                        new OptionData(OptionType.STRING, "new-url", "YouTube video URL", true)
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        JsonUserPlaylistData playlistData = new JsonUserPlaylistData(event.getMember().getIdLong());

        String newUrl = event.getOption("new-url").getAsString();
        String newName = event.getOption("name").getAsString();

        playlistData.editUrl(newName, newUrl);

        if (playlistData.isSet()) {
            event.getHook().sendMessageEmbeds(successfullyReplyEmbed()).setEphemeral(true).queue();
        }
        else {
            event.getHook().sendMessageEmbeds(notSuccessfullyReplyEmbed()).setEphemeral(true).queue();
        }
    }
}
