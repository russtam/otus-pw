package ru.rustam.otus.fbbe.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.net.ssl.HttpsURLConnection;

@EnableWebSecurity
@Configuration
public class WebConfiguration {

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
        http
                .oauth2Client()
                .and()
                .oauth2Login()
                .tokenEndpoint()
                .and()
                .userInfoEndpoint();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http
                .authorizeHttpRequests()
                .requestMatchers( "/oauth2/**", "/login/**", "/actuator/**").permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .logout()
                .logoutSuccessUrl("http://keycloak.192.168.49.2.nip.io/realms/external/protocol/openid-connect/logout?redirect_uri=http://arch.homework:8000/");
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

}
