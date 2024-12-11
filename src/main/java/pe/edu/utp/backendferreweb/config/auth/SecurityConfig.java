package pe.edu.utp.backendferreweb.config.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pe.edu.utp.backendferreweb.config.auth.filter.JwtAuthFilter;
import pe.edu.utp.backendferreweb.persistence.model.enums.ERol;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    @Value("${spring.security.frontend.url}")
    private String frontendUrl;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList(frontendUrl));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(c -> corsConfigurationSource())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest -> authRequest
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers("/api/users/**").hasAuthority(ERol.ADMIN.name())
                        .requestMatchers("/api/roles/**").hasAuthority(ERol.ADMIN.name())
                        .requestMatchers("/api/almacenes/**").hasAnyAuthority(
                                ERol.ADMIN.name(), ERol.INVENTARIO.name()
                        )
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        .requestMatchers("/api/categorias/**").hasAuthority(ERol.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers("/api/productos/**").hasAnyAuthority(
                                ERol.ADMIN.name(), ERol.INVENTARIO.name()
                        )
                        .requestMatchers("/api/unidades/**").hasAnyAuthority(
                                ERol.ADMIN.name(), ERol.INVENTARIO.name()
                        )
                        .requestMatchers("/api/proveedores/**").hasAnyAuthority(
                                ERol.ADMIN.name(), ERol.COMPRAS.name())
                        .requestMatchers("/api/cotizaciones").hasAnyAuthority(
                                ERol.ADMIN.name(), ERol.COMPRAS.name()
                        )
                        .requestMatchers("/api/ordenes-compra/aprobar/").hasAuthority(ERol.ADMIN.name())
                        .requestMatchers("/api/ordenes-compra/**").hasAnyAuthority(
                                ERol.ADMIN.name(), ERol.COMPRAS.name()
                        )
                        .requestMatchers("/api/ventas/**").hasAnyAuthority(
                                ERol.ADMIN.name(), ERol.CAJERO.name()
                        )
                        .requestMatchers("/test/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager -> sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
