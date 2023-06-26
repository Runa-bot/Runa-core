package com.kindit.bot.listeners.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class Loop implements SubCommand {
    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(getName(), "loop track");
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        event.deferReply().setEphemeral(true).queue();

        PlayerManager.getINSTANCE().loop(event.getChannel().asTextChannel());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Зациклено: " + scheduler.isLoop);
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
