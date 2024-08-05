package xyz.kindit.nekobot.config.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class MainCommand extends Command {

    @JacksonXmlProperty(localName = "SubCommand")
    public List<SubCommand> subCommands;

}
