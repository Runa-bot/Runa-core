package com.kindit.bot.listeners;

import com.kindit.bot.listeners.commands.Command;
import com.kindit.bot.listeners.commands.chatgpt.ChatGPT;
import com.kindit.bot.listeners.commands.player.Player;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private static final Command[] commands = {
        new Player(),
        new ChatGPT()
    };

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (Command command : commands) {
            try {
                command.interaction(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
