package xyz.kindit.nekobot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.kindit.nekobot.listener.CommandsListener;
import xyz.kindit.nekobot.listener.LaunchListener;
import xyz.kindit.nekobot.listener.MessageListener;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NekoBot {

    @Getter
    private static ShardManager shardManager;

    private static void run() {
        log.atInfo().log("Starting");

        shardManager = DefaultShardManagerBuilder
                .createDefault("SECRET")
                .setStatus(OnlineStatus.ONLINE)
                .build();

        shardManager.addEventListener(
                new LaunchListener(),
                new CommandsListener(),
                new MessageListener()
        );
    }

    public static void main(String[] args) {
        NekoBot.run();
    }

}
