package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) -> {
           final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
           guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
           return guildMusicManager;
        });
    }

    public void loadAndPlay(InteractionHook hook, TextChannel textChannel, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor("Добавленно в очередь");
                eb.setDescription("Объектов в очереди: " + (musicManager.scheduler.getQueue().size() + 1));
                eb.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri);
                eb.setColor(new Color(141, 66, 179));
                hook.sendMessageEmbeds(eb.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                for (AudioTrack track : tracks) {
                    musicManager.scheduler.queue(track);
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setDescription("Объектов в очереди: " + (musicManager.scheduler.getQueue().size() + 1));
                eb.setTitle("Добавлено в очередь: " + tracks.size() + " чего-то там...", trackURL);
                eb.setColor(new Color(141, 66, 179));
                hook.sendMessageEmbeds(eb.build()).queue();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
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

    public static PlayerManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
