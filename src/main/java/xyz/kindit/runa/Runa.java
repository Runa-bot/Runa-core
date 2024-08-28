package xyz.kindit.runa;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
public class Runa {

	public final JDA jda;

    public Runa(@Value("${runa.token}") String token) {
        jda = JDABuilder
				.createDefault(token)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(Runa.class, args);
	}

}
