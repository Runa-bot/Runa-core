package com.kindit.bot.commands.playlist.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.data.JsonUserPlaylistData;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AddToUserPlaylist extends Subcommand {

    public AddToUserPlaylist(String name, String description, Command parentCommand) {
        super(name, description, parentCommand);
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(userName, description)
                .addOptions(
                        new OptionData(OptionType.STRING, "url", "YouTube video URL", true),
                        new OptionData(OptionType.STRING, "name", "Set name for url", true)
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        event.deferReply().setEphemeral(true).queue();

        JsonUserPlaylistData playlistData = new JsonUserPlaylistData(event.getMember().getIdLong());
        String url = event.getOption("url").getAsString();
        String name = event.getOption("name").getAsString();
        MessageEmbed embed = Command.notSuccessfullyReplyEmbed();

        if (!isYoutubeUrl(url)) embed = Command.replyEmbed("Your link doesn't look like a link to a YouTube video", Command.BAD_COLOR);
        else if (isYoutubeUrl(name)) embed = Command.replyEmbed("Your name looks like a link to a YouTube video", Command.BAD_COLOR);
        else playlistData.addUrl(name, url);

        if (playlistData.isSet()) embed = Command.successfullyReplyEmbed();

        event.getHook().sendMessageEmbeds(embed).setEphemeral(true).queue();
    }

    private boolean isYoutubeUrl(String url) {
        return url.contains("https://youtu.be/") || url.contains("https://www.youtube.com/");
    }
}
