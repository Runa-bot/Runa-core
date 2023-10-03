package com.kindit.bot.listeners.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.listeners.commands.SubCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Queue implements SubCommand {
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(getName(), "Get the whole queue")
                .addOptions(
                        new OptionData(OptionType.STRING, "file", "Send txt file", false)
                                .addChoice("Yes", "Yes"),
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        new Thread(() -> {
            boolean ephemeralChoice;
            try {
                ephemeralChoice = event.getOption("ephemeral").equals("No");
            } catch (NullPointerException e) {
                ephemeralChoice = true;
            }

            boolean fileChoice;
            try {
                fileChoice = event.getOption("file").getAsString().equals("Yes");
            } catch (NullPointerException e) {
                fileChoice = false;
            }

            event.deferReply().setEphemeral(ephemeralChoice).queue();

            TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
            List<AudioTrack> queue = new ArrayList<>(scheduler.getQueue());

            if (scheduler.getAudioTrack() == null) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
                eb.addField("Добавить трек", "/player add", true);
                eb.setColor(Color.RED);

                event.getHook().setEphemeral(ephemeralChoice).sendMessageEmbeds(eb.build()).queue();
                return;
            }

            List<String> tracksInfo = new ArrayList<>();
            tracksInfo.add(1 + ". TITLE: " + scheduler.getAudioTrack().getInfo().title + " URL: " + scheduler.getAudioTrack().getInfo().uri + "\n");
            for (int i = 0; i < queue.size(); i++) {
                tracksInfo.add(i + 2 + ". TITLE: " + queue.get(i).getInfo().title + " URL: " + queue.get(i).getInfo().uri + "\n");
            }

            if (!fileChoice && tracksInfo.toString().length() < 4096) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Список очерди");
                eb.setDescription(tracksInfo.toString());
                eb.setColor(new Color(141, 66, 179));

                event.getHook().setEphemeral(ephemeralChoice).sendMessageEmbeds(eb.build()).queue();
                return;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
            Date date = new Date();
            String fileName = event.getGuild().getIdLong() + " " + formatter.format(date) + ".txt";

            try {
                FileWriter writer = new FileWriter(fileName);
                tracksInfo.forEach(track -> {
                    try {
                        writer.write(track);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(fileName);
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                file.delete();
            }).start();

            event.getHook().setEphemeral(ephemeralChoice).sendFiles(FileUpload.fromData(file)).queue();
        }).start();
    }
}
