package com.example.BapZip.config;

import com.example.BapZip.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig
{

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) {
        try {

            http.csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())
                    .sessionManagement((sessionManagement) ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )

                    .authorizeHttpRequests((authorizeRequests) ->
                            authorizeRequests.requestMatchers("/health").permitAll()
                                    .anyRequest().authenticated()
                    )
                    .exceptionHandling((exceptionConfig) ->
                                    exceptionConfig
                                            .authenticationEntryPoint(unauthorizedEntryPoint)
                    ); // 401 403 관련 예외처리



            ;

            http.addFilterAfter(
                    jwtAuthenticationFilter,
                    CorsFilter.class
            );
            return http.build();
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {


//                ReasonDTO dto = new ReasonDTO(HttpStatus.UNAUTHORIZED, false, "402", "사용자 인증 실패1");
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//
//                // 한글이 깨지지 않도록 인코딩 설정 추가
//                response.setCharacterEncoding("UTF-8");
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//                PrintWriter writer = response.getWriter();
//                writer.write(new ObjectMapper().writeValueAsString(dto));
//                writer.flush();

            };

}
