package com.kindit.bot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;

public class UserMessageEL extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();

        if (guildId == 688479404862472263L || guildId == 907572465998381076L || guildId == 738134731560058922L) {
            String message = event.getMessage().getContentRaw().toLowerCase(Locale.ROOT);
            String[] cummands = { "cum", "cam", "кам", "кум" };

            for (String word : cummands) {
                while (message.contains(word)) {
                    message = message.replaceFirst(word, "");
                    event.getChannel().sendMessage(":sweat_drops:").queue();
                }
            }
        }
    }
}
