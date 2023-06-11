package com.kindit.bot.listeners.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class Pause implements SubCommand {
    private final String name = "pause";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, "Pauses it");
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        PlayerManager.getINSTANCE().pause(event.getChannel().asTextChannel());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully!");
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
