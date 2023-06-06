package listeners.commands;

import lavaplayer.PlayerManager;
import lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * The main logic of the bot discord player is here
 */
public class Player extends ListenerAdapter {

    /**
     * Primary command name
     */
    private static final String name = "player";

    /**
     * Returns a description subcommand for discord
     */
    @NotNull
    public static CommandData subCommands() {
        return net.dv8tion.jda.api.interactions.commands.build.Commands.slash(name, "Audio player")
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

    /**
     * Returns a description main command for discord
     */
    @NotNull
    public static CommandData mainCommand() {
        return net.dv8tion.jda.api.interactions.commands.build.Commands.slash(name, "Audio player")
                .addOptions(
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    /**
     * Contains static methods with functionality for commands
     */
    private static class Commands {
        private static void player(SlashCommandInteractionEvent event) {
            TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
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
        private static void add(SlashCommandInteractionEvent event) {
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

        public static void skip(SlashCommandInteractionEvent event) {
            TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
            event.deferReply().setEphemeral(true).queue();

            if (event.getOption("quantity").getAsString().equals("One")) {
                scheduler.nextTrack();
            }
            else {
                scheduler.clearQueue();
            }

            embedResponse(event);
        }

        public static void pause(SlashCommandInteractionEvent event) {
            event.deferReply().setEphemeral(true).queue();

            PlayerManager.getINSTANCE().pause(event.getChannel().asTextChannel());

            embedResponse(event);
        }

        public static void resume(SlashCommandInteractionEvent event) {
            event.deferReply().setEphemeral(true).queue();

            PlayerManager.getINSTANCE().resume(event.getChannel().asTextChannel());

            embedResponse(event);
        }
    }

    /**
     * Defines interaction for commands
     * @param event Discord user data
     */
    public static void interaction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getFullCommandName().equals(name)) { Commands.player(event); }
        else if (event.getFullCommandName().equals(name + " add")) { Commands.add(event); }
        else if (event.getFullCommandName().equals(name + " skip")) { Commands.skip(event); }
        else if (event.getFullCommandName().equals(name + " pause")) { Commands.pause(event); }
        else if (event.getFullCommandName().equals(name + " resume")) { Commands.resume(event); }
    }

    /**
     * Contains an embed to reply to the user
     * @param event Discord user data
     */
    private static void embedResponse(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Successfully!");
        eb.setColor(new Color(141, 66, 179));
        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
