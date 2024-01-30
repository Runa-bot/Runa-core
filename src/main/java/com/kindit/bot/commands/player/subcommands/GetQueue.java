package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.FileUpload;

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
        return new SubcommandData(userName, description)
                .addOptions(
                        new OptionData(OptionType.STRING, "file", "Send txt file", false)
                                .addChoice("Yes", "Yes"),
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        setGuild(event.getGuild());
        boolean ephemeralChoice = !Optional.ofNullable(event.getOption("ephemeral")).isPresent();
        boolean fileChoice = Optional.ofNullable(event.getOption("file")).isPresent();
        String content = "";
        try { content = createContent(); }
        catch (NullPointerException ignored) {}

        event.deferReply().setEphemeral(ephemeralChoice).queue();

        if (!isPlaying())
            event.getHook().sendMessageEmbeds(createNoPlayEmbed()).queue();
        else if (content.length() > 4096 || fileChoice)
            event.getHook().sendFiles(FileUpload.fromData(createResponceFile(content))).setEphemeral(ephemeralChoice).queue();
        else
            event.getHook().sendMessageEmbeds(createResponceEmbed(content)).setEphemeral(ephemeralChoice).queue();
    }

    private boolean isPlaying() {
        return PlayerManager.getINSTANCE().getMusicManager(getGuild()).scheduler.getAudioTrack() != null;
    }

    private MessageEmbed createNoPlayEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
        eb.addField("Добавить трек", "/player add", true);
        eb.setColor(Command.BAD_COLOR);

        return eb.build();
    }

    private MessageEmbed createResponceEmbed(String content) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Список очерди");
        eb.setDescription(content);
        eb.setColor(Command.BOT_COLOR);
        return eb.build();
    }

    private File createResponceFile(String content) {
        File responceFile = new File(createFileName());
        writeToFile(responceFile, content);
        deleteTheFileAfter(responceFile);

        return responceFile;
    }

    private String createContent() {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(getGuild()).scheduler;
        List<AudioTrack> tracks = new ArrayList<>(scheduler.getQueue());
        StringBuilder content = new StringBuilder();
        content
                .append("[Now playing]. ")
                .append("`").append(scheduler.getAudioTrack().getInfo().title).append("`\n")
                .append("               ").append(scheduler.getAudioTrack().getInfo().uri).append("\n");
        for (int i = 0; i < tracks.size(); i++) {
            content
                    .append(i + 1).append(". ")
                    .append("`").append(tracks.get(i).getInfo().title).append("`\n")
                    .append(((Integer) (i + 1)).toString().replaceAll("\\d", " ")).append("  ")
                    .append(tracks.get(i).getInfo().uri).append("\n");
        }

        return content.toString();
    }

    private String createFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date = new Date();

        return getGuild().getIdLong() + " " + formatter.format(date) + ".txt";
    }

    private void writeToFile(File file, String content) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTheFileAfter(File file) {
        new Thread(() -> {
            try { Thread.sleep(10000); }
            catch (InterruptedException e) { throw new RuntimeException(e); }
            file.delete();
        }).start();
    }
}
