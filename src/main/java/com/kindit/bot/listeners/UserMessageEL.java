package com.kindit.bot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserMessageEL extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();

        if (guildId == 688479404862472263L || guildId == 907572465998381076L || guildId == 738134731560058922L) {
            String message = event.getMessage().getContentRaw();
            String[] cummands = { "cum", "cam", "кам", "кум" };
            for (String command : cummands) {
                if (message.contains(command)) {
                    event.getChannel().sendMessage(":sweat_drops:").queue();
                }
            }
        }
    }
}
