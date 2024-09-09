package xyz.kindit.runa;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Runa {

	@Getter
	private static JDA jda;

    public Runa(@Value("${runa.token}") String token) throws InterruptedException {
		jda = JDABuilder
				.createDefault(token)
				.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.build()
				.awaitReady();
	}

	public static void main(String[] args) {
		SpringApplication.run(Runa.class, args);
	}

}
