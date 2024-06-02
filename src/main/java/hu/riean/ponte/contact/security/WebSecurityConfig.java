package hu.riean.ponte.contact.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author riean
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/").permitAll()
                .requestMatchers("/contact/**", "/api/contact/**").hasAuthority("USER")
                .anyRequest().permitAll()
        ).formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
        ).logout((logout) -> logout.permitAll());

        return http.build();
    }

}
