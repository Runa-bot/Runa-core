package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.commands.Subcommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;


public class ShuffleQueue extends Subcommand {

    public ShuffleQueue(String name, String description, Command parentCommand) {
        super(name, description, parentCommand);
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(userName, description);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        event.deferReply().setEphemeral(true).queue();

        scheduler.shuffleQueue();

        event.getHook().sendMessageEmbeds(Command.successfullyReplyEmbed()).queue();
    }
}
