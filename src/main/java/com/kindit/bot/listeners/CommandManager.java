package com.kindit.bot.listeners;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.player.Player;
import com.kindit.bot.commands.playlist.Playlist;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private final Command[] commands = {
        new Player(),
        new Playlist()
    };

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (Command command : commands) {
            try {
                event.deferReply().setEphemeral(true).queue();
                command.interaction(event);
            } catch (Exception e) {
                String message = e.toString().length() >= 2000 ? e.toString().substring(0, 1900) : e.toString();
                String errorInfo = (new SimpleDateFormat("HH.mm.ss.SSS dd-MM-yyyy").format(new Date())) + "\n" +
                        "[GuildID]: " + event.getGuild().getIdLong() + ";\n[Guild name]: " + event.getGuild().getName() + "\n" +
                        "[ChannelID]: " + event.getChannel().getIdLong() + ";\n[Channel name]: " + event.getChannel().getName() + "\n" +
                        "[UserID]: " + event.getMember().getIdLong() + ";\n[User name]: " + event.getMember().getEffectiveName() + "\n" +
                        "[Full command name]: " + event.getFullCommandName() + "\n";
                System.out.println(errorInfo);
                event.getHook().sendMessage(
                        "`Okay error creator, you've created a error. This error: " + message + "`\n"
                                + errorInfo
                                + "`I've already filed a report on you with the error department.`"
                ).queue();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (Command command : commands) {
            try {
                command.buttonInteraction(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (Command command : commands) {
            commandDataList.add(command.getCommandData());
        }

        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands().queue();
    }
}
