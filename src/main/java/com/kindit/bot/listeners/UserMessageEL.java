package com.kindit.bot.listeners;

import com.kindit.bot.data.JsonConfig;
import com.kindit.bot.data.dto.UserKeywordDTO;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;

public class UserMessageEL extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) return;

        long guildId = event.getGuild().getIdLong();

        if (guildId == 688479404862472263L || guildId == 907572465998381076L || guildId == 738134731560058922L) {
            String message = event.getMessage().getContentRaw().toLowerCase(Locale.ROOT);
            UserKeywordDTO[] keywords = JsonConfig.getInstance().USER_KEYWORDS;

            for (UserKeywordDTO keywordDTO : keywords) {
                while (message.contains(keywordDTO.getKeyword())) {
                    message = message.replaceFirst(keywordDTO.getKeyword(), "");
                    event.getChannel().sendMessage(keywordDTO.getResponse()).queue();
                }
            }
        }
    }
}
