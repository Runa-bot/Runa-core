package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.commands.Subcommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.Objects;

public class PlayerVolume extends Subcommand {

    public PlayerVolume(String name, String description, Command parentCommand) {
        super(name, description, parentCommand);
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(userName, description)
                .addOptions(
                        new OptionData(OptionType.INTEGER, "volume", "Sound volume", true)
                                .setMinValue(0)
                                .setMaxValue(1000)
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        int volume = Objects.requireNonNull(event.getOption("volume")).getAsInt();

        event.deferReply().setEphemeral(true).queue();

        scheduler.audioPlayer.setVolume(volume);

        event.getHook().sendMessageEmbeds(Command.successfullyReplyEmbed()).setEphemeral(true).queue();
    }
}
