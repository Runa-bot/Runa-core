package com.kindit.bot.commands.playlist.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.data.JsonUserPlaylistData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class GetUserPlaylist extends Subcommand {

    public GetUserPlaylist(String name, String description, Command parentCommand) {
        super(name, description, parentCommand);
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(userName, description);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        event.deferReply().setEphemeral(true).queue();
        
        MessageEmbed responseEmbed = embedForPlaylist(event);
        File responseFile = fileForPlaylist(event);
        if (responseEmbed.getDescription().length() < 4096) {
            event.getHook().sendMessageEmbeds(responseEmbed).setEphemeral(true).queue();
        }
        else {
            event.getHook().sendFiles(FileUpload.fromData(responseFile));
        }
    }

    private MessageEmbed embedForPlaylist(SlashCommandInteractionEvent event) {
        JsonUserPlaylistData playlistData = new JsonUserPlaylistData(event.getMember().getIdLong());

        StringBuilder description = new StringBuilder();
        for (Map.Entry<String, String> entry : playlistData.getUserTracks().entrySet()) {
            description.append(entry.getKey()).append(": ");
            description.append(entry.getValue()).append("\n");
        }
        description.append(" ");

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your playlist");
        eb.setColor(Command.BOT_COLOR);
        eb.setDescription(description);
        return eb.build();
    }
    
    private File fileForPlaylist(SlashCommandInteractionEvent event) {
        JsonUserPlaylistData playlistData = new JsonUserPlaylistData(event.getMember().getIdLong());
        
        StringBuilder playlist = new StringBuilder();
        for (Map.Entry<String, String> entry : playlistData.getUserTracks().entrySet()) {
            playlist.append(entry.getKey()).append(": ");
            playlist.append(entry.getValue()).append("\n");
        }

        String fileName = createFilename(event);
        writeToFile(playlist, fileName);

        File file = new File(fileName);
        deleteAfter10seconds(file);

        return file;
    }

    private void deleteAfter10seconds(File file) {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            file.delete();
        }).start();
    }

    private void writeToFile(StringBuilder playlist, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            try {
                writer.write(playlist.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private String createFilename(SlashCommandInteractionEvent event) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date = new Date();
        String fileName = event.getGuild().getIdLong() + " " + formatter.format(date) + ".txt";
        return fileName;
    }
}
