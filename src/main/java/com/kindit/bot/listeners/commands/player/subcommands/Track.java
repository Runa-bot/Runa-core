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

public class Track implements SubCommand {
    private final String name = "track";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, "Gets track information")
                .addOptions(
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
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
            if (scheduler.audioPlayer.isPaused()) {
                eb.addField("Статус", "Пуза", true);
            }
            else {
                eb.addField("Статус", "Играет", true);
            }
            if (scheduler.isLoop) {
                eb.addField("Зациклено", "Да", true);
            }
            else {
                eb.addField("Зациклено", "Нет", true);
            }
            eb.setColor(new Color(141, 66, 179));

            event.getHook().sendMessageEmbeds(eb.build()).queue();

        }
        else {
            eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
            eb.addField("Громкость", scheduler.audioPlayer.getVolume() + "%", true);
            eb.addField("Добавить трек", "/player add", true);
            eb.setColor(Color.RED);
            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }

    }
}
