package com.kindit.bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final BlockingQueue<AudioTrack> queue;
    public final AudioPlayer audioPlayer;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    public void nextTrack() {
        this.audioPlayer.startTrack(this.queue.poll(), false);
    }

    public void clearQueue() {
        this.audioPlayer.stopTrack();
        this.queue.clear();
    }

    public void queue(AudioTrack track) {
        if (!this.audioPlayer.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }
}
