package projeto.api.taskmanager.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import projeto.api.taskmanager.configuration.authentication.CustomAuthenticationManager;
import projeto.api.taskmanager.configuration.authentication.SecurityFilter;
import projeto.api.taskmanager.configuration.authentication.TokenService;
import projeto.api.taskmanager.user.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) -> {
            request.antMatchers(HttpMethod.POST, "/users/**").permitAll()
            .anyRequest().authenticated();
        }).addFilterBefore(new SecurityFilter(tokenService,userRepository), UsernamePasswordAuthenticationFilter.class);

        http.csrf((csrf) -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new CustomAuthenticationManager();
    }
} 
