package com.kindit.bot.listeners.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class Volume implements SubCommand {
    private final String name = "volume";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData("volume", "Sound volume")
                .addOptions(
                        new OptionData(OptionType.INTEGER, "volume", "Sound volume", true)
                                .setMinValue(0)
                                .setMaxValue(1000)
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        int volume = event.getOption("volume").getAsInt();


        event.deferReply().setEphemeral(true).queue();

        scheduler.audioPlayer.setVolume(volume);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Volume set: " + volume + "%");
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
