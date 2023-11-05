package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.Objects;

public class Volume extends SubCommand {

    public Volume() {
        super("volume", "Sound volume");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, description)
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

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Volume set: " + volume + "%");
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
