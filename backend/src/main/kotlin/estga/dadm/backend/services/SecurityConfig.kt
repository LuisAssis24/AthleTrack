package estga.dadm.backend.services

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * Classe de configuração de segurança da aplicação.
 *
 * Esta configuração define as regras de segurança para as requisições HTTP,
 * desabilitando a proteção CSRF e permitindo todas as requisições sem autenticação.
 */
@Configuration
class SecurityConfig {

    /**
     * Cria e configura o filtro de segurança da aplicação.
     *
     * - Desativa a proteção CSRF.
     * - Permite todas as requisições HTTP sem necessidade de autenticação.
     *
     * @param http Instância de HttpSecurity para configuração.
     * @return Instância de SecurityFilterChain configurada.
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