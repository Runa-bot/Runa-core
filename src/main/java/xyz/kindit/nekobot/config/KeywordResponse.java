package xyz.kindit.nekobot.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class KeywordResponse {

    @JacksonXmlProperty(localName = "KeywordList")
    public List<Keyword> keywordList;

    @JacksonXmlProperty(localName = "ResponseList")
    public List<Response> responseList;

    @JacksonXmlProperty(localName = "ServerWhiteList")
    public List<Server> serverWhiteList;

    @JacksonXmlProperty(localName = "ServerBlackList")
    public List<Server> serverBlackList;

}
