package xyz.kindit.nekobot.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Server {

    @JacksonXmlProperty(localName = "Server")
    public String value;

}
