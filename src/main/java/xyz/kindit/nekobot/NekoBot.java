package xyz.kindit.nekobot;

import lombok.Getter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.kindit.nekobot.listener.CommandsListener;
import xyz.kindit.nekobot.listener.LaunchListener;
import xyz.kindit.nekobot.listener.MessageListener;

public class NekoBot {

    @Getter
    private static ShardManager shardManager;

    private static void run() {
        shardManager = DefaultShardManagerBuilder
                .createDefault("Nzk0NzA4MDI0Mzg3MTc0NDYw.G3xafS.JZFopmQSBeOVLLZwwfPn-NM9d9ZyQdIkenjg_U")
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
