package com.kindit.bot;

import com.kindit.bot.data.JSONConfig;
import com.kindit.bot.data.StreamParser;
import com.kindit.bot.data.TwitchCategory;
import com.kindit.bot.listeners.CommandManager;
import com.kindit.bot.listeners.UserMessageEL;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class NekoBot {
    private final ShardManager shardManager;

    public NekoBot() throws LoginException {
        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(JSONConfig.TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(
                new UserMessageEL(),
                new CommandManager()
        );

        new Thread(() -> {
            while (true) {
                shardManager.setActivity(Activity.streaming("Minecraft", new StreamParser(TwitchCategory.MINECRAFT).getStreamUrl()));
                try {
                    Thread.sleep(1800000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            NekoBot nekoBot = new NekoBot();
        } catch (LoginException e) {
            System.out.println("Error: Provided bot token is invalid");
        }
    }
}
