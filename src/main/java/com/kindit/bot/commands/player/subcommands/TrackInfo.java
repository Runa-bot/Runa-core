package com.kindit.bot.commands.player.subcommands;

import com.kindit.bot.lavaplayer.PlayerManager;
import com.kindit.bot.lavaplayer.TrackScheduler;
import com.kindit.bot.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.Optional;

public class TrackInfo extends SubCommand {
    public TrackInfo() {
        super("track-info", "Gets track information");
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(name, description)
                .addOptions(
                        new OptionData(OptionType.STRING, "ephemeral", "Should this message be ephemeral?", false)
                                .addChoice("No", "No")
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        boolean ephemeral = !Optional.ofNullable(event.getOption("ephemeral")).isPresent();
        event.deferReply().setEphemeral(ephemeral).queue();

        if (scheduler.audioPlayer.getPlayingTrack() != null) {
            event.getHook().sendMessageEmbeds(getEmbed(scheduler)).addActionRow(
                    scheduler.audioPlayer.isPaused()
                            ? Button.primary("play", Emoji.fromFormatted("U+25B6"))
                            : Button.primary("pause", Emoji.fromFormatted("U+23F8")),
                    Button.primary("skip", Emoji.fromFormatted("U+23ED")),
                    Button.primary("loop", Emoji.fromFormatted("U+1F501")),
                    Button.primary("shuffel", Emoji.fromFormatted("U+1F500"))

            ).queue();
        } else {
            event.getHook().sendMessageEmbeds(getEmbed(scheduler)).queue();
        }
    }

    @Override
    public void buttonInteraction(ButtonInteractionEvent event) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(event.getChannel().asTextChannel().getGuild()).scheduler;
        if ("skip".equals(event.getComponentId())) {
            scheduler.nextTrack();

            event.editMessageEmbeds(getEmbed(scheduler)).queue();
        } else if ("pause".equals(event.getComponentId())) {
            scheduler.pause();

            event.editButton(Button.primary("play", Emoji.fromFormatted("U+25B6"))).queue();
        } else if ("play".equals(event.getComponentId())) {
            scheduler.resume();

            event.editButton(Button.primary("pause", Emoji.fromFormatted("U+23F8"))).queue();
        } else if ("loop".equals(event.getComponentId())) {
            PlayerManager.getINSTANCE().loop(event.getChannel().asTextChannel());

            event.editMessageEmbeds(getEmbed(scheduler)).queue();
        } else if ("shuffel".equals(event.getComponentId())) {
            scheduler.shuffelQueue();

            event.editMessageEmbeds(getEmbed(scheduler)).queue();
        }
    }

    private MessageEmbed getEmbed(TrackScheduler scheduler) {
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
        }
        else {
            eb.setTitle("Кажется сейчас ничего не играет... :/", "https://youtu.be/dQw4w9WgXcQ?t=43");
            eb.addField("Громкость", scheduler.audioPlayer.getVolume() + "%", true);
            eb.addField("Добавить трек", "/player add", true);
            eb.setColor(Color.RED);
        }
        return eb.build();
    }
}
