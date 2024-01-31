package com.kindit.bot.data.dto;

public class UserKeywordDTO {
    private final String keyword;
    private final String response;

    public String getKeyword() {
        return keyword;
    }

    public String getResponse() {
        return response;
    }

    public UserKeywordDTO(String keyword, String response) {
        this.keyword = keyword;
        this.response = response;
    }
}
