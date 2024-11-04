package co.streamly.streamly_backend.config;

import co.streamly.streamly_backend.service.FirebaseUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import static org.springframework.security.config.Customizer.withDefaults;

import org.checkerframework.checker.units.qual.m;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final FirebaseUserService firebaseUserService;

    public SecurityConfig(FirebaseUserService firebaseUserService) {
        this.firebaseUserService = firebaseUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // Permite todas las rutas
            );

        return http.build();
    }
  //  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  //      http
//.addFilterBefore(firebaseTokenFilter(), UsernamePasswordAuthenticationFilter.class)
  //              .csrf(AbstractHttpConfigurer::disable)
  //              .cors(withDefaults())
  //              .authorizeHttpRequests(auth -> auth
  //                      .requestMatchers("/streamly/**","/streamly/combos/**", "/streamly/accounts/**",
    //                            "/streamly/allproducts", "/streamly/users/validate-token",
  //                              "/streamly/users/register", "/streamly/users/**",
    //                            "/streamly/users/role", "/streamly/admin/products/**",
    //                            "/streamly/metadata/services/**","localhost:8080/streamly/stocks/**")
     //                   .permitAll()
      //                  .requestMatchers("/streamly/admin/**", "streamly/admin/products",
       //                         "streamly/metadata/services/**")
        //                .hasRole("ADMIN"));

     //   System.out.println("SecurityFilterChain configurado");
    //    System.out.println("FirebaseUserService: " + firebaseUserService);

    //    return http.build();
    //}

    @Bean
    public FirebaseTokenFilter firebaseTokenFilter() {
        return new FirebaseTokenFilter(firebaseUserService);
    }

}
