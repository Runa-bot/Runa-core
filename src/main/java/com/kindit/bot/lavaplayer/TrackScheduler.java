package com.kindit.bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final BlockingQueue<AudioTrack> queue;
    private AudioTrack audioTrack;
    public boolean isLoop = false;
    public final AudioPlayer audioPlayer;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext && this.isLoop) {
            this.audioPlayer.startTrack(this.audioTrack, false);
        } else if (endReason.mayStartNext) {
            nextTrack();
        }

    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        this.audioTrack = track.makeClone();
    }

    public void nextTrack() {
        this.audioPlayer.startTrack(this.queue.remove(), false);
    }

    public void clearQueue() {
        this.audioPlayer.stopTrack();
        this.queue.clear();
    }

    public void queue(AudioTrack track) {
        if (!this.audioPlayer.startTrack(track, true)) {
            this.queue.add(track);
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void shuffelQueue() {
        List<AudioTrack> tracks = new ArrayList<>(this.queue);
        Collections.shuffle(tracks);
        this.queue.clear();
        this.queue.addAll(tracks);
    }
}
