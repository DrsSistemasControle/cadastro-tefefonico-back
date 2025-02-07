package br.com.drs.cadastrotelefonico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CadastroTelefonicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CadastroTelefonicoApplication.class, args);
    }

}
