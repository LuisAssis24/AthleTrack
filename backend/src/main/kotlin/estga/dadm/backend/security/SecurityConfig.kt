package estga.dadm.backend.security

import org.springframework.context.annotation.*
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
