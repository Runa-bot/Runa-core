package com.kindit.bot.commands.chatgpt;

import com.kindit.bot.data.JsonConfig;
import com.kindit.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ChatGPT extends Command {
    private ChatGPT() {
        super("chat-gpt", "ChatGPT 3.0");
    }

    public static ChatGPT createCommand() {
        return new ChatGPT();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(userName, description)
                .addOptions(
                        new OptionData(OptionType.STRING, "text", "Your text for chat-gpt", true)
                                .setMaxLength(256)
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getName().equals(userName)) { return; }
        new Thread(() -> {
            event.deferReply().queue();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(Objects.requireNonNull(event.getMember()).getNickname(), null, event.getMember().getEffectiveAvatarUrl());
            eb.setTitle(Objects.requireNonNull(event.getOption("text")).getAsString());
            try {
                eb.setDescription(chatGPT(Objects.requireNonNull(event.getOption("text")).getAsString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            eb.setFooter("OpenAI", "https://img.uxwing.com/wp-content/themes/uxwing/download/brands-social-media/chatgpt-icon.png");
            eb.setColor(new Color(25, 195, 125));

            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }).start();
    }

    public String chatGPT(String text) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", JsonConfig.getInstance().GPT_KEY_API);

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 2049);
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).orElse(null);

        assert output != null;
        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }
}
