package listeners;

import listeners.commands.Command;
import listeners.commands.Player;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private static final Command[] commands = {
            new Player()
    };
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (Command command : commands) {
            command.interaction(event);
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (Command command : commands) {
            commandDataList.add(command.getSubCommandsData());
        }

        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (Command command : commands) {
            commandDataList.add(command.getMainCommandData());
        }

        event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}
