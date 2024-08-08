/*
 * MIT License
 * Copyright (c) 2024 Kindit
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.kindit.nekobot.config.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import xyz.kindit.nekobot.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "NekoBot")
@SuppressWarnings("unused")
public class XmlConfig extends Config {

    @JacksonXmlProperty(isAttribute = true)                 private String token;
    @JacksonXmlProperty(localName = "Commands")             private List<MainCommandImpl> commandList;
    @JacksonXmlProperty(localName = "KeywordResponses")     private List<KeywordResponseImpl> keywordResponseList;
    @JacksonXmlProperty(localName = "SlashCommands")        private List<SlashCommandImpl> slashCommandList;

    @SneakyThrows(IOException.class)
    public Config deserialize(File configFile) {
        return new XmlMapper().readValue(configFile, XmlConfig.class);
    }

    @SneakyThrows(IOException.class)
    public Config deserialize(String config) {
        return new XmlMapper().readValue(config, XmlConfig.class);
    }
}
