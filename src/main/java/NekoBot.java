import listeners.CommandManager;
import listeners.UserMessageEL;
import listeners.commands.Player;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class NekoBot {
    public final ShardManager shardManager;

    public NekoBot() throws LoginException {
        String TOKEN = "NzMxMDMzODUyMjI2Njk5Mjk0.G8Xzvo.VWOQNRobPqBzj6Y6oazSSyAmFoEM8p8ndDZpK0";

        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.streaming("Minecraft", "https://www.youtube.com/watch?v=HRbW75fYLvo"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(
                new UserMessageEL(),
                new CommandManager()
        );
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
