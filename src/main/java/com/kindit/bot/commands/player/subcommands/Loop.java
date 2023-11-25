package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.commands.SubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;


public class Loop extends SubCommand {
    public Loop() {
        super("loop", "loop track");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, description);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        event.deferReply().setEphemeral(true).queue();

        PlayerManager.getINSTANCE().loop(event.getChannel().asTextChannel());

        event.getHook().sendMessageEmbeds(successfullyReplyEmbed()).setEphemeral(true).queue();
    }
}
