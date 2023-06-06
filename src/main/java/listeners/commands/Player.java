package listeners.commands;

import lavaplayer.PlayerManager;
import lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

public class Player extends ListenerAdapter {
    public static CommandData subCommands() {
        return Commands.slash("player", "Audio player")
                .addSubcommands(
                        new SubcommandData("add", "Add track in queue")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "url", "YouTube video URL", true),
                                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                                .addChoice("No", "No")
                                ),
                        new SubcommandData("skip", "Skip track")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "quantity", "Skip track", true)
                                                .addChoice("One", "One")
                                                .addChoice("All", "All")

                                ),
                        new SubcommandData("pause", "Pause track"),
                        new SubcommandData("resume", "Resume track")
                );
    }

    public static CommandData command() {
        return Commands.slash("player", "Audio player")
                .addOptions(
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    public static void interaction(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        if (event.getFullCommandName().equals("player")) {
            boolean ephemeral;
            try {
                ephemeral = event.getOption("ephemeral").equals("No");
            } catch (NullPointerException e) {
                ephemeral = true;
            }

            event.deferReply().setEphemeral(ephemeral).queue();

            EmbedBuilder eb = new EmbedBuilder();
            if (scheduler.getQueue().isEmpty()) {
                eb.setAuthor("Сейчас играет", "https://youtu.be/dQw4w9WgXcQ?t=43","https://cdn.discordapp.com/attachments/804354568128692285/1115581488688275486/5ed58522f979098ff382f83b42cda3f8.png");
                eb.setTitle(scheduler.audioPlayer.getPlayingTrack().getInfo().title, scheduler.audioPlayer.getPlayingTrack().getInfo().uri);
                eb.setDescription("Всего объектов в очереди: " + (scheduler.getQueue().size() + 1));
                eb.setColor(new Color(141, 66, 179));
            }
            else {
                eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
                eb.setColor(new Color(141, 66, 179));
            }

            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }
        else if (event.getFullCommandName().equals("player add")) {
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
        else if (event.getFullCommandName().equals("player skip")) {
            event.deferReply().setEphemeral(true).queue();

            if (event.getOption("quantity").getAsString().equals("One")) {
                scheduler.nextTrack();
            }
            else {
                scheduler.clearQueue();
            }

            embedResponse(event);
        }
        else if (event.getFullCommandName().equals("player pause")) {
            event.deferReply().setEphemeral(true).queue();

            PlayerManager.getINSTANCE().pause(event.getChannel().asTextChannel());

            embedResponse(event);
        }
        else if (event.getFullCommandName().equals("player resume")) {
            event.deferReply().setEphemeral(true).queue();

            PlayerManager.getINSTANCE().resume(event.getChannel().asTextChannel());

            embedResponse(event);
        }
    }

    public static void embedResponse(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully!");
        eb.setColor(new Color(141, 66, 179));
        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
