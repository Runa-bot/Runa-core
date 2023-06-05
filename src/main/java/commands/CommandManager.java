package commands;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();
        OptionData[] playOptionData = {
                new OptionData(
                        OptionType.STRING,
                        "url",
                        "YouTube video",
                        true
                ),
                new OptionData(
                        OptionType.BOOLEAN,
                        "ephemeral",
                        "Should this message be ephemeral?",
                        false
                )
        };
        commandDataList.add(Commands.slash("play", "Audio player").addOptions(playOptionData));
        commandDataList.add(Commands.slash("skip", "Skip audio"));
        commandDataList.add(Commands.slash("skip_all", "Skip all audio"));

        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().updateCommands().queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("play")) {
            boolean ephemeral;
            try {
                ephemeral = event.getOption("ephemeral").getAsBoolean();
            }
            catch (NullPointerException e) { ephemeral = true; }

            event.deferReply().setEphemeral(ephemeral).queue();

            // Get url
            String url = event.getOption("url").getAsString();

            // Test user in voice channel
            if (!event.getMember().getVoiceState().inAudioChannel()) {
                event.getHook().sendMessage("You need to be in a voice channel for this command work.").queue();
            }
            else {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberchannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                if (!audioManager.isConnected()) { audioManager.openAudioConnection(memberchannel); }
                PlayerManager.getINSTANCE().loadAndPlay(event.getHook().setEphemeral(ephemeral), event.getChannel().asTextChannel(), url);
            }
        }
        else if (command.equals("skip")) {
                event.deferReply().setEphemeral(true).queue();

                PlayerManager.getINSTANCE().skip(event.getChannel().asTextChannel());

                event.getHook().sendMessage("Successfully!").queue();
        }
        else if (command.equals("skip_all")) {
            event.deferReply().setEphemeral(true).queue();

            PlayerManager.getINSTANCE().skipAll(event.getChannel().asTextChannel());

            event.getHook().sendMessage("Successfully!").queue();
        }
    }
}
