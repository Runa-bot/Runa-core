package com.kindit.bot.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonUserPlaylistData {
    private final long discordId;
    private final Map<String, String> userTracks;
    private final File jsonFile;
    private boolean operationStatus = false;

    public boolean isSet() {
        boolean result = operationStatus;
        operationStatus = false;
        return result;
    }

    public Map<String, String> getUserTracks() {
        return userTracks;
    }

    public JsonUserPlaylistData(long discordId) {
        this.discordId = discordId;
        jsonFile = new File(new File("").getAbsolutePath() + "\\data\\usersPlaylist\\" + discordId + ".json");
        userTracks = getData();
    }

    public void addUrl(String name, String url) {
        userTracks.put(name, url);

        createJson(userTracks);

        operationStatus = true;
    }

    public void deleteUrl(String name) {
        userTracks.remove(name);

        createJson(userTracks);

        operationStatus = true;
    }

    public void editUrl(String name, String url) {
        url = url.equals("") ? userTracks.get(name) : url;

        userTracks.put(name, url);

        createJson(userTracks);

        operationStatus = true;
    }

    private Map<String, String> getData() {
        JSONParser parser = new JSONParser();
        Reader reader = null;
        if (jsonFile.exists()) {
            try {
                reader = new FileReader(jsonFile);
            } catch (FileNotFoundException e) {
                System.out.println("User playlist not found :/");
            }
        }
        else {
            if (createJson()) {
                try {
                    reader = new FileReader(jsonFile);
                } catch (FileNotFoundException e) {
                    System.out.println("User playlist not found :/");
                }
            }
        }

        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(reader);
            assert reader != null;
            reader.close();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        JSONArray userTracksJSON = (JSONArray) jsonObject.get("playlist");

        Map<String, String> userTracks = new HashMap<>();
        for (Object obj : userTracksJSON) {
            JSONObject jsonObj = (JSONObject) obj;
            userTracks.put(jsonObj.get("name").toString(), jsonObj.get("url").toString());
        }

        return userTracks;
    }

    private boolean createJson() {
        String path = new File("").getAbsolutePath();

        File folder = new File(path + "\\data\\usersPlaylist");
        folder.mkdirs();

        File file = new File(folder + "\\" + discordId + ".json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObject.put("playlist", jsonArray);

        try {
            FileWriter writer = new FileWriter(jsonFile);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonFile.exists();
    }

    private boolean createJson(Map<String, String> userTracks) {
        String path = new File("").getAbsolutePath();

        File folder = new File(path + "\\data\\usersPlaylist");
        folder.mkdirs();

        File file = new File(folder + "\\" + discordId + ".json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<String, String> entry : userTracks.entrySet()) {
            JSONObject jsonTrack = new JSONObject();
            jsonTrack.put("name", entry.getKey());
            jsonTrack.put("url", entry.getValue());
            jsonArray.add(jsonTrack);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playlist", jsonArray);

        try {
            FileWriter writer = new FileWriter(jsonFile);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonFile.exists();
    }
}
