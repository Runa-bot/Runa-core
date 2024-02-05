package com.kindit.bot.listeners;

import com.kindit.bot.data.JsonConfig;
import com.kindit.bot.data.dto.UserKeywordsDTO;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Locale;

public class MessageManager extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) return;

        String message = event.getMessage().getContentRaw().toLowerCase(Locale.ROOT);
        UserKeywordsDTO[] keywords = JsonConfig.getInstance().USER_KEYWORDS;

        for (UserKeywordsDTO keywordDTO : keywords) {
            if (Arrays.stream(keywordDTO.getGuilds()).noneMatch(obj -> obj == event.getGuild().getIdLong())) continue;

            for (String keyword : keywordDTO.getKeywords()) {
                while (message.contains(keyword)) {
                    message = message.replaceFirst(keyword, " ");
                    event.getChannel().sendMessage(keywordDTO.getResponse()).queue();
                }
            }
        }
    }
}
