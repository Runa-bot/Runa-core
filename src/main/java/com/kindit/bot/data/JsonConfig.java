package com.kindit.bot.data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConfig {
    public final String TOKEN;
    public final String GPT_KEY_API;
    private final String configName = "NekoBot";
    private final JSONObject config;
    private static JsonConfig instance;

    public JsonConfig() {
        this.config = getConfig();
        this.TOKEN = config.get("token").toString();
        this.GPT_KEY_API = "Bearer " + getCommand("chat-gpt").get("GPTKeyAPI");
    }

    public static JsonConfig getInstance() {
        if (instance == null) {
            instance = new JsonConfig();
        }
        return instance;
    }

    public void createDefaultConfig() throws IOException {
        String json = "{\n" +
                "  \"token\": \"Your bot token\",\n" +
                "  \"commands\" : {\n" +
                "    \"player\": {\n" +
                "      \"name\": \"player\",\n" +
                "      \"active\": \"true\",\n" +
                "      \"subcommands\": {\n" +
                "        \"add\": {\n" +
                "          \"name\": \"add\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"loop\": {\n" +
                "          \"name\": \"loop\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"pause\": {\n" +
                "          \"name\": \"pause\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"queue\": {\n" +
                "          \"name\": \"queue\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"resume\": {\n" +
                "          \"name\": \"resume\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"shuffel\": {\n" +
                "          \"name\": \"shuffel\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"skip\": {\n" +
                "          \"name\": \"skip\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"volume\": {\n" +
                "          \"name\": \"volume\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"track-info\": {\n" +
                "          \"name\": \"track-info\",\n" +
                "          \"active\": \"true\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"playlist\": {\n" +
                "      \"name\": \"track-list\",\n" +
                "      \"active\": \"true\",\n" +
                "      \"subcommands\": {\n" +
                "        \"add\": {\n" +
                "          \"name\": \"add\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"delete\": {\n" +
                "          \"name\": \"delete\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"edit\": {\n" +
                "          \"name\": \"edit\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"get\": {\n" +
                "          \"name\": \"get\",\n" +
                "          \"active\": \"true\"\n" +
                "        },\n" +
                "        \"play\": {\n" +
                "          \"name\": \"play\",\n" +
                "          \"active\": \"true\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"chat-gpt\": {\n" +
                "      \"GPTKeyAPI\": \"Your GPT API key\",\n" +
                "      \"name\": \"chat-gpt\",\n" +
                "      \"active\": \"false\",\n" +
                "      \"subcommands\": {}\n" +
                "    }\n" +
                "  }\n" +
                "}";

        FileWriter file = new FileWriter(configName + ".json");
        file.write(json);
        file.close();
    }

    private JSONObject getConfig()  {
        JSONObject object = null;
        try {
            JSONParser parser = new JSONParser();
            object = (JSONObject) parser.parse(new FileReader(configName + ".json"));
        } catch (IOException | ParseException e) {
            try {
                createDefaultConfig();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return object;
    }

    public JSONObject getCommand(String name) {
        JSONObject commands = (JSONObject) config.get("commands");

        return (JSONObject) commands.get(name);
    }

    public JSONObject getSubCommand(String commandName, String subCommandName) {
        JSONObject subcommands = (JSONObject) getCommand(commandName).get("subcommands");

        return (JSONObject) subcommands.get(subCommandName);
    }
}
