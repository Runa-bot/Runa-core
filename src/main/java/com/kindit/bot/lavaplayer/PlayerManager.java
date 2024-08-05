package com.kindit.bot.lavaplayer;

import com.kindit.bot.commands.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.AndroidTestsuiteWithThumbnail;
import dev.lavalink.youtube.clients.MusicWithThumbnail;
import dev.lavalink.youtube.clients.WebWithThumbnail;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,
                new MusicWithThumbnail(),
                new WebWithThumbnail(),
                new AndroidTestsuiteWithThumbnail()
        );
        audioPlayerManager.registerSourceManager(ytSourceManager);

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager, YoutubeAudioSourceManager.class);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) -> {
           final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
           guild.getAudioManager().setSendingHandler(guildMusicManager.sendHandler);
           return guildMusicManager;
        });
    }

    public void loadAndPlay(InteractionHook hook, TextChannel textChannel, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                hook.sendMessageEmbeds(getEmbedBuilderForTrack(audioTrack, musicManager)).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                hook.sendMessageEmbeds(getEmbedBuilderForPlayList(tracks, musicManager, trackURL)).queue();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public void loadAndPlay(TextChannel textChannel, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                tracks.forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    private MessageEmbed getEmbedBuilderForPlayList(List<AudioTrack> tracks, GuildMusicManager musicManager, String trackURL) {
        int tracksSize = tracks.size();
        if (tracks.size() > 47) {
            tracksSize = 47;
        }
        tracks.forEach(musicManager.scheduler::queue);
        StringBuilder tracksInfo = new StringBuilder();
        for (int i = 0; i < tracksSize; i++) {
            tracksInfo
                    .append(i + 1)
                    .append(". ")
                    .append(tracks.get(i).getInfo().title)
                    .append("\n")
                    .append(tracks.get(i).getInfo().uri)
                    .append("\n");
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Добавлено в очередь: " + tracks.size() + " чего-то там...", trackURL);
        eb.setColor(Command.GOOD_COLOR);
        eb.setDescription(tracksInfo);

        return eb.build();
    }

    @NotNull
    private MessageEmbed getEmbedBuilderForTrack(AudioTrack audioTrack, GuildMusicManager musicManager) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Добавленно в очередь");
        eb.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri);
        eb.setColor(Command.GOOD_COLOR);
        return eb.build();
    }

    public void pause(TextChannel textChannel) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        if (!musicManager.audioPlayer.isPaused()) {
            musicManager.audioPlayer.setPaused(true);
        }
    }

    public void resume(TextChannel textChannel) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        if (musicManager.audioPlayer.isPaused()) {
            musicManager.audioPlayer.setPaused(false);
        }
    }

    public void loop(TextChannel textChannel) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        musicManager.scheduler.isLoop = !musicManager.scheduler.isLoop;
    }

    public void queueLoop(TextChannel textChannel) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        musicManager.scheduler.isQueueLoop = !musicManager.scheduler.isQueueLoop;
    }

    public static PlayerManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
