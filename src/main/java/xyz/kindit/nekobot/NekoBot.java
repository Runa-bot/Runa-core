/*
 * MIT License
 * Copyright (c) 2024 Kindit
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.kindit.nekobot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.kindit.nekobot.config.Config;
import xyz.kindit.nekobot.config.ConfigFactory;
import xyz.kindit.nekobot.listener.CommandsListener;
import xyz.kindit.nekobot.listener.LaunchListener;
import xyz.kindit.nekobot.listener.MessageListener;

import java.io.File;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NekoBot {

    @Getter
    private static ShardManager shardManager;
    public static final Config config = ConfigFactory.createConfig(new File("NekoBot.xml"));

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
