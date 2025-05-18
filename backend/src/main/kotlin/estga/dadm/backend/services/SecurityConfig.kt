package estga.dadm.backend.services

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }  // Desativa proteção CSRF
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll() // Permite todos os pedidos sem autenticação
            }
        return http.build()
    }
}