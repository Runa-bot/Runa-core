package listeners.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class Command extends ListenerAdapter {
    private String name;

    protected String getName() {
        return name;
    }

    protected Command(String name) {
        this.name = name;
    }

    public CommandData getMainCommandData() {
        return null;
    }

    public CommandData getSubCommandsData() {
        return null;
    }

    public void interaction(SlashCommandInteractionEvent event) {}
}
