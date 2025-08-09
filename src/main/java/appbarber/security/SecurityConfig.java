package appbarber.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;
    public SecurityConfig(JwtService jwtService) { this.jwtService = jwtService; }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // swagger
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        // magic link público (solicitar/usar)
                        .requestMatchers("/api/public/**").permitAll()
                        // lectura pública opcional (podés restringir si querés)
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // login y registro
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // magic link login
                        .requestMatchers( "/api/public/magic/link**").permitAll()
                        // resto requiere ADMIN
                        .anyRequest().hasRole("ADMIN")
                )
                .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults()); // útil para probar 401
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

}
