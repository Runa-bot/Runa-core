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

package xyz.kindit.nekobot.config;

import java.io.File;

public class ConfigFactory {
    public static Config createConfig(File config) {
        return createConfig(config, getConfigType(config));
    }

    public static Config createConfig(File config, ConfigType configType) {
        return switch (configType) {
            case ConfigType.XML -> new XmlConfig().deserialize(config);
        };
    }

    public static Config createConfig(String config, ConfigType configType) {
        return switch (configType) {
            case ConfigType.XML -> new XmlConfig().deserialize(config);
        };
    }

    private static ConfigType getConfigType(File file) {
        if (file.getAbsolutePath().matches("(.*).xml"))
            return ConfigType.XML;

        throw new NullPointerException("ConfigType is not defined");
    }
}
