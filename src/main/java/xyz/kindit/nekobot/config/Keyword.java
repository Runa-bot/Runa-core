package xyz.kindit.nekobot.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Keyword {

    @JacksonXmlProperty(localName = "Keyword")
    @JacksonXmlText
    public String value;

}
