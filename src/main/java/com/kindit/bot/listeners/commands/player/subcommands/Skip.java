package com.kindit.bot.listeners.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class Skip implements SubCommand {
    private final String name = "skip";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, "Skip track")
                .addOptions(
                        new OptionData(OptionType.STRING, "quantity", "Skip track", true)
                                .addChoice("One", "One")
                                .addChoice("All", "All")

                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        event.deferReply().setEphemeral(true).queue();

        if (event.getOption("quantity").getAsString().equals("One")) {
            scheduler.nextTrack();
        }
        else {
            scheduler.clearQueue();
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully!");
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
