package com.kindit.bot.listeners.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class Shuffel implements SubCommand {
    @Override
    public String getName() {
        return "shuffel";
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(getName(), "Shuffles the queue");
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        event.deferReply().setEphemeral(true).queue();

        scheduler.shuffelQueue();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully!");
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
