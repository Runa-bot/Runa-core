package com.kindit.bot.listeners.commands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

public class Player extends Command {
    public Player() { super("player", "Audio player"); }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOptions(
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    @Override
    public CommandData getSubCommandsData() {
        return Commands.slash(getName(), getDescription())
                .addSubcommands(
                        // Add - player add
                        new SubcommandData("add", "Add track in queue")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "url", "YouTube video URL", true),
                                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                                .addChoice("No", "No")
                                ),
                        // Skip - player skip
                        new SubcommandData("skip", "Skip track")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "quantity", "Skip track", true)
                                                .addChoice("One", "One")
                                                .addChoice("All", "All")

                                ),
                        new SubcommandData("status", "Set status")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "status", "Status type", true)
                                                .addChoice("Play", "Play")
                                                .addChoice("Pause", "Pause")
                                ),
                        // Volume - player volume
                        new SubcommandData("volume", "Sound volume")
                                .addOptions(
                                        new OptionData(OptionType.INTEGER, "volume", "Sound volume", true)
                                                .setMinValue(0)
                                                .setMaxValue(1000)
                                )
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        if (event.getFullCommandName().equals(getName())) { player(event); }
        else if (event.getFullCommandName().equals(getName() + " add")) { add(event); }
        else if (event.getFullCommandName().equals(getName() + " status")) { status(event); }
        else if (event.getFullCommandName().equals(getName() + " skip")) { skip(event); }
        else if (event.getFullCommandName().equals(getName() + " volume")) { volume(event); }
    }

    private void player(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        boolean ephemeral;
        try {
            ephemeral = event.getOption("ephemeral").equals("No");
        } catch (NullPointerException e) {
            ephemeral = true;
        }

        event.deferReply().setEphemeral(ephemeral).queue();

        EmbedBuilder eb = new EmbedBuilder();
        if (scheduler.audioPlayer.getPlayingTrack() != null) {
            eb.setAuthor("Сейчас играет", "https://youtu.be/dQw4w9WgXcQ?t=43");
            eb.setTitle(scheduler.audioPlayer.getPlayingTrack().getInfo().title, scheduler.audioPlayer.getPlayingTrack().getInfo().uri);
            eb.addField("Объектов в очереди", String.valueOf(scheduler.getQueue().size() + 1), true);
            eb.addField("Громкость", scheduler.audioPlayer.getVolume() + "%", true);
            if (scheduler.audioPlayer.isPaused()) {
                eb.addField("Статус", "Пуза", true);
            }
            else {
                eb.addField("Статус", "Играет", true);
            }
            eb.setColor(new Color(141, 66, 179));

            event.getHook().sendMessageEmbeds(eb.build()).queue();

        }
        else {
            eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
            eb.addField("Громкость", scheduler.audioPlayer.getVolume() + "%", true);
            eb.addField("Добавить трек", "/player add", true);
            eb.setColor(Color.RED);
            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }


    }

    private void add(SlashCommandInteractionEvent event) {
        boolean ephemeral;
        try {
            ephemeral = event.getOption("ephemeral").equals("No");
        } catch (NullPointerException e) {
            ephemeral = true;
        }

        event.deferReply().setEphemeral(ephemeral).queue();

        String url = event.getOption("url").getAsString();

        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.getHook().sendMessage("You need to be in a voice channel for this command work.").queue();
        } else {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberchannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            if (!audioManager.isConnected()) {
                audioManager.openAudioConnection(memberchannel);
            }
            PlayerManager.getINSTANCE().loadAndPlay(event.getHook().setEphemeral(ephemeral), event.getChannel().asTextChannel(), url);
        }
    }

    private void skip(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        event.deferReply().setEphemeral(true).queue();

        if (event.getOption("quantity").getAsString().equals("One")) {
            scheduler.nextTrack();
        }
        else {
            scheduler.clearQueue();
        }

        embedResponse(event);
    }

    private void status(SlashCommandInteractionEvent event) {
        String result = event.getOption("status").getAsString();

        event.deferReply().setEphemeral(true).queue();

        if (result.equals("Pause")) {
            PlayerManager.getINSTANCE().pause(event.getChannel().asTextChannel());
        }
        else {
            PlayerManager.getINSTANCE().resume(event.getChannel().asTextChannel());
        }

        embedResponse(event);
    }

    private void volume(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        int volume = event.getOption("volume").getAsInt();


        event.deferReply().setEphemeral(true).queue();

        scheduler.audioPlayer.setVolume(volume);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Volume set: " + volume + "%");
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
    }

    private void embedResponse(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully!");
        eb.setColor(Color.GREEN);
        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
