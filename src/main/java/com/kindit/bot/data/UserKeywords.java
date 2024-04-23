package com.kindit.bot.data;

public class UserKeywords {
    private final Long[] guilds;
    private final String[] keywords;
    private final String response;


    public Long[] getGuilds() {
        return guilds;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getResponse() {
        return response;
    }

    public UserKeywords(Long[] guilds, String[] keywords, String response) {
        this.guilds = guilds;
        this.keywords = keywords;
        this.response = response;
    }
}
