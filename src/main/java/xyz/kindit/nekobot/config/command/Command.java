package xyz.kindit.nekobot.config.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

abstract class Command {

    @JacksonXmlProperty(localName = "name", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "displayName", isAttribute = true)
    public String displayName;

    @JacksonXmlProperty(localName = "active", isAttribute = true)
    public Boolean active;

}
