package estga.dadm.backend.services

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * Configuração de segurança da aplicação.
 */
@Configuration
class SecurityConfig {

    /**
     * Configura o filtro de segurança para permitir todas as requisições e desabilitar CSRF.
     */
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