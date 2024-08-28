package xyz.kindit.runa;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Level;

@Log
@SpringBootApplication
public class RunaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RunaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.log(Level.INFO, "There's nothing here :/");
	}
}
