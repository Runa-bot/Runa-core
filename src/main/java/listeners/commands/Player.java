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
     * Returns a description main command for discord
     */
    @NotNull
    public static CommandData mainCommand() {
        // Player - player
        return net.dv8tion.jda.api.interactions.commands.build.Commands.slash(name, "Audio player")
                .addOptions(
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    /**
     * Returns a description subcommand for discord
     */
    @NotNull
    public static CommandData subCommands() {
        return net.dv8tion.jda.api.interactions.commands.build.Commands.slash(name, "Audio player")
                .addSubcommands(
                        // Add - player add
                        new SubcommandData("add", "Add track in queue")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "url", "YouTube video URL", true),
                                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                                .addChoice("No", "No")
                                ),
                        // Skip - player skip
                        new SubcommandData("skip", "Skip track")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "quantity", "Skip track", true)
                                                .addChoice("One", "One")
                                                .addChoice("All", "All")

                                ),
                        // Pause - player pause
                        new SubcommandData("pause", "Pause track"),
                        // Resume - player resume
                        new SubcommandData("resume", "Resume track"),
                        // Volume - player volume
                        new SubcommandData("volume", "Sound volume")
                                .addOptions(
                                        new OptionData(OptionType.INTEGER, "volume", "Sound volume", true)
                                                .setMinValue(0)
                                                .setMaxValue(1000)
                                )
                );
    }

    /**
     * Contains static methods with functionality for commands
     */
    private static class Commands {
        /**
         * Player embed
         */
        private static void player(@NotNull SlashCommandInteractionEvent event) {
            TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
            boolean ephemeral;
            try {
                ephemeral = event.getOption("ephemeral").equals("No");
            } catch (NullPointerException e) {
                ephemeral = true;
            }

            event.deferReply().setEphemeral(ephemeral).queue();

            EmbedBuilder eb = new EmbedBuilder();
            if (scheduler.audioPlayer.getPlayingTrack() != null) {
                eb.setAuthor("Сейчас играет", "https://youtu.be/dQw4w9WgXcQ?t=43");
                eb.setTitle(scheduler.audioPlayer.getPlayingTrack().getInfo().title, scheduler.audioPlayer.getPlayingTrack().getInfo().uri);
                eb.addField("Объектов в очереди", String.valueOf(scheduler.getQueue().size() + 1), true);
                eb.addField("Громкость", scheduler.audioPlayer.getVolume() + "%", true);
                eb.setColor(new Color(141, 66, 179));

                event.getHook().sendMessageEmbeds(eb.build()).queue();

            }
            else {
                eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
                eb.addField("Объектов в очереди", String.valueOf(scheduler.getQueue().size() + 1), true);
                eb.addField("Громкость", scheduler.audioPlayer.getVolume() + "%", true);
                eb.setColor(Color.RED);
                event.getHook().sendMessageEmbeds(eb.build()).queue();
            }


        }

        /**
         * Adds url to the queue
         */
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

        /**
         * Skips the current track
         */
        public static void skip(@NotNull SlashCommandInteractionEvent event) {
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

        /**
         * Pauses the player
         */
        public static void pause(@NotNull SlashCommandInteractionEvent event) {
            event.deferReply().setEphemeral(true).queue();

            PlayerManager.getINSTANCE().pause(event.getChannel().asTextChannel());

            embedResponse(event);
        }

        /**
         * Resume the player
         */
        public static void resume(@NotNull SlashCommandInteractionEvent event) {
            event.deferReply().setEphemeral(true).queue();

            PlayerManager.getINSTANCE().resume(event.getChannel().asTextChannel());

            embedResponse(event);
        }

        /**
         * Adjusts the sound of the player
         */
        public static void volume(@NotNull SlashCommandInteractionEvent event) {
            TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
            int volume = event.getOption("volume").getAsInt();


            event.deferReply().setEphemeral(true).queue();

            scheduler.audioPlayer.setVolume(volume);

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Volume set: " + volume + "%");
            eb.setColor(Color.GREEN);

            event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
        }

        /**
         * Contains an embed to reply to the user
         */
        private static void embedResponse(@NotNull SlashCommandInteractionEvent event) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Successfully!");
            eb.setColor(Color.GREEN);
            event.getHook().sendMessageEmbeds(eb.build()).queue();
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
        else if (event.getFullCommandName().equals(name + " volume")) { Commands.volume(event); }
    }
}
