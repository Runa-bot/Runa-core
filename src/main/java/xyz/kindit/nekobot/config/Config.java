package xyz.kindit.nekobot.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import xyz.kindit.nekobot.config.command.MainCommand;

import java.io.File;
import java.util.List;

@Getter
@ToString
@JacksonXmlRootElement(localName = "NekoBot")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {

    @JacksonXmlProperty(localName = "Token")
    private String token;

    @JacksonXmlProperty(localName = "Commands")
    private List<MainCommand> mainCommands;

    @JacksonXmlProperty(localName = "KeywordResponseList")
    private List<KeywordResponse> keywordResponseList;

    @SneakyThrows
    public static Config deserialize(File xmlFile) {
        XmlMapper xmlMapper = new XmlMapper();

        return xmlMapper.readValue(xmlFile, Config.class);
    }

    @SneakyThrows
    public static Config deserialize(String xml) {
        XmlMapper xmlMapper = new XmlMapper();

        return xmlMapper.readValue(xml, Config.class);
    }

}
