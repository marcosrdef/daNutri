package com.danutri.nutricao.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.danutri.nutricao.api.entidade.Usuario;
import com.danutri.nutricao.api.enums.Perfil;
import com.danutri.nutricao.api.repository.UserRepository;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class NutricaoDaNutriApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutricaoDaNutriApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, passwordEncoder);
		};
	}
	
	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		Usuario admin = new Usuario();
		admin.setEmail("marcosrdef@gmail.com");
		admin.setSenha(passwordEncoder.encode("123456"));
		admin.setPerfil(Perfil.cliente);
		
		Usuario find = userRepository.findByEmail("marcosrdef@gmail.com");
		if (find == null) {
			userRepository.save(admin);
		}
	}

}
