package com.kindit.bot;

import com.kindit.bot.data.JsonConfig;
import com.kindit.bot.data.StreamParser;
import com.kindit.bot.data.TwitchCategory;
import com.kindit.bot.listeners.CommandManager;
import com.kindit.bot.listeners.MessageManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class NekoBot {
    private final ShardManager shardManager;

    public ShardManager getShardManager() {
        return shardManager;
    }

    public NekoBot() throws LoginException {
        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(JsonConfig.getInstance().TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(
                new MessageManager(),
                new CommandManager()
        );

        setUptimeStatus(shardManager);

        //setStream(shardManager);
    }

    private void setUptimeStatus(ShardManager shardManager) {
        new Thread(() -> {
            while (true) {
                long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                shardManager.setActivity(Activity.watching(convertMillisecondToTime(uptime)));
                try {
                    Thread.sleep(5500);
                } catch (InterruptedException e) { throw new RuntimeException(e); }
            }
        }).start();
    }

    private static String convertMillisecondToTime(long millisecond) {
        long days = TimeUnit.MILLISECONDS.toDays(millisecond);
        millisecond -= TimeUnit.DAYS.toMillis(days);

        long hours = TimeUnit.MILLISECONDS.toHours(millisecond);
        millisecond -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecond);
        millisecond -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecond);

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    private void setStream(ShardManager shardManager) {
        new Thread(() -> {
            StreamParser streamParser = new StreamParser(TwitchCategory.MINECRAFT);

            while (true) {
                shardManager.setActivity(Activity.streaming("Minecraft", streamParser.getStreamUrl()));
                try {
                    Thread.sleep(1800000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            NekoBot nekoBot = new NekoBot();
        } catch (LoginException e) {
            System.out.println("Error: Provided bot token is invalid");
        }
    }
}
