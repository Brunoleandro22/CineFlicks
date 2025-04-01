package br.com.cineflicks;

import br.com.cineflicks.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CineflicksApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CineflicksApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal();
        principal.exibeMenu();


    }
}
