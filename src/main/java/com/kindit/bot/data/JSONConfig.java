package com.kindit.bot.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONConfig {
    public final static String TOKEN;
    public final static String GPTKeyAPI;

    static {
        try {
            TOKEN = getToken();
            GPTKeyAPI = "Bearer " + getGPTKeyAPI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setJSON() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TOKEN", "Your bot token");
        jsonObject.put("GPTKeyAPI", "Your GPT API key");

        FileWriter file = new FileWriter("NekoBot.json");
        file.write(jsonObject.toJSONString());
        file.close();
    }

    private static String getToken() throws IOException {
        JSONObject object = null;
        try {
            JSONParser parser = new JSONParser();
            object = (JSONObject) parser.parse(new FileReader("NekoBot.json"));
        } catch (IOException | ParseException e) {
            setJSON();
        }
        return (String) object.get("TOKEN");
    }

    private static String getGPTKeyAPI() throws IOException {
        JSONObject object = null;
        try {
            JSONParser parser = new JSONParser();
            object = (JSONObject) parser.parse(new FileReader("NekoBot.json"));
        } catch (IOException | ParseException e) {
            setJSON();
        }
        return (String) object.get("GPTKeyAPI");
    }
}
