package com.kindit.bot.listeners.commands.chatgpt;

import com.kindit.bot.data.JSONConfig;
import com.kindit.bot.listeners.commands.Command;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatGPT implements Command {
    private final String name = "chat-gpt";

    @Override
    public String getName() {
        return "chat-gpt";
    }

    @Override
    public SubCommand[] getSubCommands() {
        return new SubCommand[] {
            new SubCommand() {
                @Override
                public String getName() {
                    return "3_0";
                }

                @Override
                public SubcommandData getSubCommandData() {
                    return new SubcommandData(getName(), "ChatGPT 3.0")
                            .addOptions(
                                    new OptionData(OptionType.STRING, "text", "Your text for chat-gpt", true)
                                            .setMaxLength(256)
                            );
                }

                @Override
                public void interaction(SlashCommandInteractionEvent event) throws Exception {
                    event.deferReply().queue();

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(event.getMember().getNickname(), null, event.getMember().getEffectiveAvatarUrl());
                    eb.setTitle(event.getOption("text").getAsString());
                    eb.setDescription(chatGPT(event.getOption("text").getAsString()));
                    eb.setFooter("OpenAI", "https://img.uxwing.com/wp-content/themes/uxwing/download/brands-social-media/chatgpt-icon.png");
                    eb.setColor(new Color(25, 195, 125));

                    event.getHook().sendMessageEmbeds(eb.build()).queue();
                }

                public String chatGPT(String text) throws Exception {
                    String url = "https://api.openai.com/v1/completions";
                    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Authorization", JSONConfig.GPTKeyAPI);

                    JSONObject data = new JSONObject();
                    data.put("model", "text-davinci-003");
                    data.put("prompt", text);
                    data.put("max_tokens", 2049);
                    data.put("temperature", 1.0);

                    con.setDoOutput(true);
                    con.getOutputStream().write(data.toString().getBytes());

                    String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                            .reduce((a, b) -> a + b).get();

                    return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
                }
            }
        };
    }

    @Override
    public CommandData getCommandData() {
        List<SubcommandData> subcommandDataList = new ArrayList<>();

        for (SubCommand command : getSubCommands()) {
            subcommandDataList.add(command.getSubCommandData());
        }

        return Commands.slash(getName(), "ChatGPT").addSubcommands(subcommandDataList);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getName().equals(name)) { return; }
        for (SubCommand command : getSubCommands()) {
            if (event.getFullCommandName().equals(name + " " + command.getName())) { command.interaction(event); }
        }
    }
}
