package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.commands.Subcommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;

public class Resume extends SubCommand {

    public Resume() {
        super("resume", "Resuming");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(userName, description);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        PlayerManager.getINSTANCE().resume(event.getChannel().asTextChannel());

        event.getHook().sendMessageEmbeds(Command.successfullyReplyEmbed()).queue();
    }
}
