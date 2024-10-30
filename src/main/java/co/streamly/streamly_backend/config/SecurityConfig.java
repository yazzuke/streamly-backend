package co.streamly.streamly_backend.config;

import co.streamly.streamly_backend.service.FirebaseUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final FirebaseUserService firebaseUserService;

    public SecurityConfig(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults()) // Habilita CORS en Spring Security
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/streamly/combos/**", "/streamly/accounts/**", "/streamly/allproducts", "/streamly/users/**").permitAll()
                .requestMatchers("/streamly/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(firebaseTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }

    @Bean
    public FirebaseTokenFilter firebaseTokenFilter() {
        return new FirebaseTokenFilter(firebaseUserService);
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(org.springframework.security.core.userdetails.User.withUsername("admin")
                .password("{noop}adminpass")
                .roles("ADMIN")
                .build());
        manager.createUser(org.springframework.security.core.userdetails.User.withUsername("user")
                .password("{noop}userpass")
                .roles("USER")
                .build());
        return manager;
    }
}
