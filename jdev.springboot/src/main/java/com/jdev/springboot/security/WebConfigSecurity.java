package com.jdev.springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebConfigSecurity {

    // Configuração do SecurityFilterChain para proteger URLs
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Desativa CSRF
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/", "/home", "/materialize/**").permitAll() // Permitir acesso público a páginas e arquivos estáticos
                    .anyRequest().authenticated() // Requer autenticação para outras URLs
                )
            .formLogin(form -> form
                .permitAll() // Permite acesso à página de login
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll() // Permite logout
            );
        return http.build();
    }

    // Define o UserDetailsService com um usuário em memória
    @Bean
    public UserDetailsService userDetailsService() {
        
    	var userDetailsManager = new InMemoryUserDetailsManager();

        // Cria um usuário em memória
        userDetailsManager.createUser(
            User.withUsername("andre")
                .password("$2a$10$0.2/FfTOiLeYkZRgYDOJDOmR1/s0EL/SQjOsGktzPEHb2WpJkB7S2") // Senha em texto puro (somente para testes)
                .roles("ADMIN")
                .build()
        );

        return userDetailsManager;
    }

    // Define um PasswordEncoder (sem codificação, apenas para testes)
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }*/
    
    // Define o PasswordEncoder para validar senhas criptografadas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

    // Define o AuthenticationManager para ser usado no sistema
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
