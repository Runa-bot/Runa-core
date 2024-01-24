package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.commands.SubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;


public class Pause extends SubCommand {
    public Pause() {
        super("pause", "Pauses it");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, description);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        PlayerManager.getINSTANCE().pause(event.getChannel().asTextChannel());

        event.getHook().sendMessageEmbeds(successfullyReplyEmbed()).queue();
    }
}
