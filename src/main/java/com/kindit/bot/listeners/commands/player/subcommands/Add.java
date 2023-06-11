package com.kindit.bot.listeners.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.AudioManager;

public class Add implements SubCommand {
    private final String name = "add";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, "Add track in queue")
                .addOptions(
                        new OptionData(OptionType.STRING, "url", "YouTube video URL", true),
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        boolean ephemeral;
        try {
            ephemeral = event.getOption("ephemeral").equals("No");
        } catch (NullPointerException e) {
            ephemeral = true;
        }

        event.deferReply().setEphemeral(ephemeral).queue();

        String url = event.getOption("url").getAsString();

        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.getHook().sendMessage("You need to be in a voice channel for this command work.").queue();
        } else {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberchannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            if (!audioManager.isConnected()) {
                audioManager.openAudioConnection(memberchannel);
            }
            PlayerManager.getINSTANCE().loadAndPlay(event.getHook().setEphemeral(ephemeral), event.getChannel().asTextChannel(), url);
        }
    }
}
