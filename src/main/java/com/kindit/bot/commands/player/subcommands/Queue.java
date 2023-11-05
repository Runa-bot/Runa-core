package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.commands.SubCommand;
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

public class Queue extends SubCommand {
    public Queue() {
        super("queue", "Get the whole queue");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, description)
                .addOptions(
                        new OptionData(OptionType.STRING, "file", "Send txt file", false)
                                .addChoice("Yes", "Yes"),
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        boolean ephemeralChoice = !Optional.ofNullable(event.getOption("ephemeral")).isPresent();
        boolean fileChoice = Optional.ofNullable(event.getOption("file")).isPresent();
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        List<AudioTrack> queue = new ArrayList<>(scheduler.getQueue());

        event.deferReply().setEphemeral(ephemeralChoice).queue();

        if (scheduler.getAudioTrack() == null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
            eb.addField("Добавить трек", "/player add", true);
            eb.setColor(Color.RED);

            event.getHook().setEphemeral(ephemeralChoice).sendMessageEmbeds(eb.build()).queue();
            return;
        }

        if (fileChoice || !sendEmbed(event, ephemeralChoice, scheduler, queue)) {
            sendFile(event, ephemeralChoice, scheduler, queue);
        }
    }

    private boolean sendEmbed(SlashCommandInteractionEvent event, boolean ephemeral, TrackScheduler scheduler, List<AudioTrack> queue) {
        StringBuffer tracksInfo = new StringBuffer();
        tracksInfo
                .append(1 + ". ")
                .append(scheduler.getAudioTrack().getInfo().title)
                .append("\n")
                .append(scheduler.getAudioTrack().getInfo().uri)
                .append("\n");
        for (int i = 0; i < queue.size(); i++) {
            tracksInfo
                    .append(i + 2)
                    .append(". ")
                    .append(queue.get(i).getInfo().title)
                    .append("\n")
                    .append(queue.get(i).getInfo().uri)
                    .append("\n");
        }

        if (tracksInfo.toString().length() > 4096) {
            return false;
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Список очерди");
            eb.setDescription(tracksInfo);
            eb.setColor(new Color(141, 66, 179));
            event.getHook().setEphemeral(ephemeral).sendMessageEmbeds(eb.build()).queue();
            return true;
        }
    }

    private void sendFile(SlashCommandInteractionEvent event, boolean ephemeral, TrackScheduler scheduler, List<AudioTrack> queue) {
        List<String> tracksInfo = new ArrayList<>();
        tracksInfo.add(1 + ". TITLE: " + scheduler.getAudioTrack().getInfo().title + " URL: " + scheduler.getAudioTrack().getInfo().uri + "\n");
        for (int i = 0; i < queue.size(); i++) {
            tracksInfo.add(i + 2 + ". TITLE: " + queue.get(i).getInfo().title + " URL: " + queue.get(i).getInfo().uri + "\n");
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

        event.getHook().setEphemeral(ephemeral).sendFiles(FileUpload.fromData(file)).queue();
    }
}
